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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
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

    @Operation(summary = "新增用户信息", description = "新增用户信息")
    @PostMapping("/{Add}")
    public Integer setLeaveService(@RequestBody Leave leave) {
        leaveService.setLeaveMapper(leave);
        return 1;
    }


    @Operation(summary = "查询报销单", description = "查询报销单")
    @PostMapping("/reimbursements")
    public void synchronousData() {


    }
    @Operation(summary = "查询报销单", description = "查询报销单")
    @PostMapping("/reimbursementsa")
    public void getlist(){
        JSONObject params = new JSONObject();
        params.put("createdAtStart", TimestampExample.getStartTimeStamp());
        params.put("createdAtEnd",  TimestampExample.getEndTimeStamp());
        params.put("formSubTypeBizCode",  goOut);
        FormDetailDto dto=maycurAuthService.getFormDetail("SQ2503194893");
//        List<FormDetail> list = maycurAuthService.getList(params);
//        for (FormDetail dto : list) {
//           // FormDetailDto formDetailDto = maycurAuthService.getFormDetail(dto.getFormCode());
//
//        }
    }
}
