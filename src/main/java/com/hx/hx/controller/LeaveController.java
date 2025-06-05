package com.hx.hx.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hx.hx.model.dao.FormDetail;
import com.hx.hx.model.dao.FormDetailDto;
import com.hx.hx.model.entity.Leave;
import com.hx.hx.service.LeaveService;
import com.hx.hx.service.MaycurAuthService;
import com.hx.hx.tool.TimestampExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关操作接口")
public class LeaveController {


    @Autowired
    private LeaveService leaveService;

    @Autowired
    private MaycurAuthService maycurAuthService;

    @Value("${my.property.goOut}")
    private String goOut;
    @Value("${my.property.chuChai}")
    private String chuChai;
    @Value("${my.property.status}")
    private String status;


    @Operation(summary = "新增用户信息", description = "新增用户信息")
    @PostMapping("/{Add}")
    public Integer setLeaveService(@RequestBody Leave leave) {
        leaveService.setLeaveMapper(leave);
        return 1;
    }


    @Operation(summary = "获取指定时间段内的出差单", description = "获取指定时间段内的出差单")
    @PostMapping("/chuChai")
    public void synchronousData(@RequestParam String startTime,@RequestParam String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long startTimeStamp = formatter.parse(startTime).getTime();
            long endTimeStamp = formatter.parse(endTime).getTime();
            JSONObject params = new JSONObject();
//            params.put("createdAtStart", startTimeStamp);
//            params.put("createdAtEnd",  endTimeStamp);
            params.put("approvedAtStart",  startTimeStamp);
            params.put("approvedAtEnd",  endTimeStamp);
            params.put("formSubTypeBizCode",  chuChai);
            params.put("formStatus",status);
            List<FormDetail> list = maycurAuthService.getList(params);
            for (FormDetail dto : list) {
                FormDetailDto formDetailDto = maycurAuthService.getFormDetail(dto.getFormCode());
                Leave leave=maycurAuthService.saveLeave(formDetailDto);
                log.info("插入出差申请单成功"+leave.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Operation(summary = "获取指定时间段的外出单", description = "获取指定时间段的外出单")
    @PostMapping("/goOut")
    public void getlist(@RequestParam String startTime,@RequestParam String endTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long startTimeStamp = formatter.parse(startTime).getTime();
            long endTimeStamp = formatter.parse(endTime).getTime();
            JSONObject params = new JSONObject();
//            params.put("createdAtStart", startTimeStamp);
//            params.put("createdAtEnd",  endTimeStamp);
            params.put("approvedAtStart",  startTimeStamp);
            params.put("approvedAtEnd",  endTimeStamp);
            params.put("formSubTypeBizCode",  goOut);
            params.put("formStatus",status);
            List<FormDetail> list = maycurAuthService.getList(params);
            for (FormDetail dto : list) {
                FormDetailDto formDetailDto = maycurAuthService.getFormDetail(dto.getFormCode());
                Leave leave=maycurAuthService.saveLeave(formDetailDto);
                log.info("插入外出申请单成功"+leave.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
