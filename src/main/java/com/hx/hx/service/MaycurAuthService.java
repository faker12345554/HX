package com.hx.hx.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.hx.hx.mapper.LeaveMapper;
import com.hx.hx.model.dao.*;
import com.hx.hx.model.entity.Leave;
import com.hx.hx.tool.TimestampExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;


import java.util.*;


@Slf4j
@Service
public class MaycurAuthService {

    @Value("${my.property.url}")
    private String authUrl;

    @Value("${my.property.appCode}")
    private String appCode;

    @Value("${my.property.appSecret}")
    private String appSecret;
    @Value("${my.property.goOut}")
    private String goOut;
    @Value("${my.property.chuChai}")
    private String chuChai;


    @Autowired
    private LeaveMapper leavemapper;


    private String entCode;

    private String token;

    private final RestTemplate restTemplate = new RestTemplate();


    long timestamp;

    public String getSecret() {
        timestamp = System.currentTimeMillis();

        return DigestUtils.sha256Hex(appSecret + ":" + appCode + ":" + timestamp);
    }

    // 获取访问令牌
    public String getAccessToken() {
        String signature = getSecret();
        log.debug("生成的签名" + signature);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject();
        params.put("appCode", appCode);
        params.put("secret", signature);
        params.put("timestamp", timestamp);
        String url = authUrl + "/api/openapi/auth/login";
        log.debug("请求地址" + url);
        HttpEntity<String> request = new HttpEntity<>(params.toJSONString(), headers);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url, request, JSONObject.class);
        log.debug("获取的返回参数" + response);
        if (response.getBody().getJSONObject("data") == null) {
            log.debug("认证接口失败,参数" + params);
        }

        entCode = response.getBody().getJSONObject("data").getString("entCode");
        token = response.getBody().getJSONObject("data").getString("tokenId");
        return response.getBody().getJSONObject("data").getString("tokenId");
    }


    public ResponseEntity<JSONObject> getReimburseList(JSONObject params) {
        String reimburseUrl = authUrl + "/api/openapi/form/v2/preconsume";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("tokenId", token);
        httpHeaders.set("entCode", entCode);
        HttpEntity<String> request = new HttpEntity<>(params.toJSONString(), httpHeaders);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(reimburseUrl, request, JSONObject.class);
        log.debug("获取申请单列表返回" + response.getBody().get("data").toString());
        if (response.getBody().get("data") == null) {
            log.debug("获取申请单列表接口失败，参数:" + params);
        }
        return response;


    }

    public FormDetailDto getFormDetail(String formCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("tokenId", token);
        headers.set("entCode", entCode);
        String url = authUrl + "/api/openapi/form/preconsume/" + formCode;
        ResponseEntity<JSONObject> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), JSONObject.class);
        log.debug("获取申请单详情返回" + response);
        if (response.getBody().get("data") == null) {
            log.debug("获取申请单详情接口失败，参数：" + formCode, response);
        }
        JSONObject jsonData = response.getBody().getJSONObject("data");
        ObjectMapper mapper = new ObjectMapper();
        FormDetailDto user;
        try {
            user = mapper.readValue(jsonData.toString(), FormDetailDto.class);
        } catch (JsonProcessingException e) {
            log.debug("数据转换失败" + jsonData);
            throw new RuntimeException(e);
        }

        return user;
    }

    public List<FormDetail> getList(JSONObject params) {
        ResponseEntity<JSONObject> jsonObject = getReimburseList(params);
        JSONObject jsonData = jsonObject.getBody().getJSONObject("data");
        JSONArray listArray = jsonData.getJSONArray("list");
        List<FormDetail> list = listArray.toJavaList(FormDetail.class);
        return list;
    }

    public int saveLeave(FormDetailDto formDetailDto) {
        List<TravelDto> travelDtoList = formDetailDto.getTravelRouteList();
        int count = 0;

        if (travelDtoList == null || travelDtoList.isEmpty()) {
            return 0;
        }

        for (TravelDto dto : travelDtoList) {
            Object value = null;
            if (dto.getTravelPartnerInfo() == null || dto.getTravelPartnerInfo().equals("")) {
                Leave leave = new Leave();
                leave.setName(formDetailDto.getApplicantEmployeeName());
                leave.setWorkNumber(formDetailDto.getApplicantEmployeeId());
                // 设置时间
                timeDto time = dto.getTravelTime();
                if (time != null) {
                    leave.setStartTime(new Date(time.getStartTime()));
                    leave.setEndTime(new Date(time.getEndTime()));
                    leave.setHours(TimestampExample.getHourDiff(time.getStartTime(), time.getEndTime()));
                }
                // 设置其他属性
                leave.setDays(dto.getTravelDays());
                leave.setStatus(formDetailDto.getFormStatus());
                leave.setType("goOut".equals(formDetailDto.getFormSubTypeBizCode()) ? "1" : "2");
                leave.setSubjectMatter(formDetailDto.getFormSubTypeName());
                //leave.setWorkNumber(formDetailDto.getFillEmployeeId());
                leave.setFormCode(formDetailDto.getFormCode());
                // 保存
                leave = leavemapper.save(leave);

            } else {
                value = dto.getTravelPartnerInfo().get("internalTravelPartner");
                // 处理 internalTravelPartner 字段
                if (value instanceof List<?>) {
                    StringBuilder partnerNames = new StringBuilder();
                    for (Object obj : (List<?>) value) {
                        Leave leave = new Leave();
                        if (obj instanceof InternalTravelPartner) {
                            InternalTravelPartner partner = (InternalTravelPartner) obj;
                            if (partner.getName() != null) {
                                leave.setName(partner.getName());
                                leave.setWorkNumber(partner.getEmployeeId());
                                partnerNames.append(partner.getName()).append(", ");
                            }
                        } else if (obj instanceof Map) {
                            // JSON反序列化的情况
                            Map<?, ?> map = (Map<?, ?>) obj;
                            Object nameObj = map.get("name");
                            leave.setName(nameObj.toString());
                            Object workNumber = map.get("employeeId");
                            leave.setWorkNumber(workNumber.toString());
                        }
                        // 设置时间
                        timeDto time = dto.getTravelTime();
                        if (time != null) {
                            leave.setStartTime(new Date(time.getStartTime()));
                            leave.setEndTime(new Date(time.getEndTime()));
                            leave.setHours(TimestampExample.getHourDiff(time.getStartTime(), time.getEndTime()));
                        }
                        // 设置其他属性
                        leave.setDays(dto.getTravelDays());
                        leave.setStatus(formDetailDto.getFormStatus());
                        leave.setType("goOut".equals(formDetailDto.getFormSubTypeBizCode()) ? "1" : "2");
                        leave.setSubjectMatter(formDetailDto.getFormSubTypeName());
                        //leave.setWorkNumber(formDetailDto.getFillEmployeeId());
                        leave.setFormCode(formDetailDto.getFormCode());
                        // 保存
                        leave = leavemapper.save(leave);
                        count++;
                    }
                }
            }


        }
//                // 去除最后的逗号
//                if (partnerNames.length() > 0) {
//                    partnerNames.setLength(partnerNames.length() - 2);
//                    leave.setName(partnerNames.toString());
//                }


        return count;
    }

}
