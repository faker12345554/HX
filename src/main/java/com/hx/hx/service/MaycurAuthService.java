package com.hx.hx.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;
import com.hx.hx.model.dao.FormDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class MaycurAuthService {

    @Value("${my.property.url}")
    private String authUrl;

    @Value("${my.property.appCode}")
    private String appCode;

    @Value("${my.property.appSecret}")
    private String appSecret;

    private String entCode;

    private final RestTemplate restTemplate = new RestTemplate();

    // 接口频率控制[4]()
    private final RateLimiter rateLimiter = RateLimiter.create(10);

    public String getSecret(){
        return DigestUtils.sha256Hex(appSecret + ":" + appCode + ":" + System.currentTimeMillis());
    }
    // 获取访问令牌
    public String getAccessToken() {
        String signature = getSecret();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject();
        params.put("appCode",  appCode);
        params.put("appSecret",  signature);
        params.put("timestamp",  System.currentTimeMillis());

        String url=authUrl+"/api/openapi/auth/login";
        HttpEntity<String> request = new HttpEntity<>(params.toJSONString(),  headers);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(url,  request, JSONObject.class);
        if(response.getBody().getJSONObject("data")==null){
            log.debug("认证接口失败,参数"+params,response);
        }

        entCode=response.getBody().getJSONObject("data").getString("entCode");
        return response.getBody().getJSONObject("data").getString("tokenId");
    }

    public JSONObject getReimburseList(Map<String, Object> params) {
        String token = getAccessToken();
        String reimburseUrl=authUrl+"/api/openapi/form/v2/preconsume";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",  "Bearer " + token);
        headers.set("entCode",entCode);
        headers.set("Content-Type",  "application/json");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(
                reimburseUrl,
                HttpMethod.POST,
                request,
                JSONObject.class
        );
        if(response.getBody().getJSONObject("data")==null){
            log.debug("获取申请单列表接口失败，参数:"+params,response);
        }
        return response.getBody();
    }



    public FormDetail getFormDetail(String formCode) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("token",  token);
        headers.set("endcode",  entCode);
        String url = "/api/openapi/form/preconsume/{formCode}";
        ResponseEntity<FormDetail> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), FormDetail.class);
        if(response.getBody()==null){
            log.debug("获取申请单详情接口失败，参数："+formCode,response);
        }
        return response.getBody();
    }
}
