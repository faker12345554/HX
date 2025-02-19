package com.hx.hx.controller;

import com.alibaba.fastjson.JSONObject;
import com.hx.hx.model.dao.FormDetail;
import com.hx.hx.model.entity.Leave;
import com.hx.hx.service.LeaveService;
import com.hx.hx.service.MaycurAuthService;
import com.hx.hx.tool.TimestampExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关操作接口")
public class LeaveController {


    @Value("${my.property.goOut}")
    private String goOut;
    @Value("${my.property.chuChai}")
    private String chuChai;
    @Autowired
    private LeaveService leaveService;

    @Autowired
    private MaycurAuthService maycurAuthService;

    @Operation(summary = "新增用户信息", description = "新增用户信息")
    @PostMapping("/{Add}")
    public Integer setLeaveService(@RequestBody Leave leave) {
        leaveService.setLeaveMapper(leave);
        return 1;
    }

    @Operation(summary = "查询报销单", description = "查询报销单")
    @PostMapping("/reimbursements")
    public JSONObject queryReimbursements() {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", TimestampExample.getStartTimeStamp());
        params.put("endTime",  TimestampExample.getEndTimeStamp());
        params.put("formSubTypeBizCode",chuChai);
//        params.put("page",  query.getPage());
//        params.put("size",  query.getSize());
        return maycurAuthService.getReimburseList(params);
    }


    public void synchronousData(){
        JSONObject jsonObject=new JSONObject();
        if (jsonObject.get("list")!=null){
            List<FormDetail> list=(List<FormDetail>)jsonObject.get("list");
            for (FormDetail dto : list) {
                Leave leave=new Leave();
                leave.setName(dto.getEmployeeName());
                leave.setType(dto.getFormSubTypeBizCode());
               // leave.setStartTime(dto.);
            }
        }
    }
}
