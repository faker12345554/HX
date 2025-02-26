package com.hx.hx.scheduled;

import com.alibaba.fastjson.JSONObject;
import com.hx.hx.model.dao.FormDetail;
import com.hx.hx.model.dao.FormDetailDto;
import com.hx.hx.model.entity.Leave;
import com.hx.hx.service.LeaveService;
import com.hx.hx.service.MaycurAuthService;
import com.hx.hx.tool.TimestampExample;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HoupuScheduled {


    @Autowired
    private LeaveService leaveService;

    @Autowired
    private MaycurAuthService maycurAuthService;

    @Value("${my.property.goOut}")
    private String goOut;
    @Value("${my.property.chuChai}")
    private String chuChai;
    // 每天23:00:00（晚上11点）执行
    @Scheduled(cron = "0 0 23 * * ?")
    public void task1() {
        JSONObject params = new JSONObject();
        params.put("createdAtStart", TimestampExample.getStartTimeStamp());
        params.put("createdAtEnd",  TimestampExample.getEndTimeStamp());
        params.put("formSubTypeBizCode",  chuChai);
        List<FormDetail> list = maycurAuthService.getList(params);
        for (FormDetail dto : list) {
            FormDetailDto formDetailDto = maycurAuthService.getFormDetail(dto.getFormCode());
            Leave leave=maycurAuthService.saveLeave(formDetailDto);
            log.info("插入出差申请单成功",leave.getId());
        }
    }
    // 每天凌晨1:00:00（晚上11点）执行
    @Scheduled(cron = "0 0 01 * * ?")
    public void task2() {
        JSONObject params = new JSONObject();
        params.put("createdAtStart", TimestampExample.getStartTimeStamp());
        params.put("createdAtEnd",  TimestampExample.getEndTimeStamp());
        params.put("formSubTypeBizCode",  goOut);
        List<FormDetail> list = maycurAuthService.getList(params);
        for (FormDetail dto : list) {
            FormDetailDto formDetailDto = maycurAuthService.getFormDetail(dto.getFormCode());
            Leave leave=maycurAuthService.saveLeave(formDetailDto);
            log.info("插入外出申请单成功",leave.getId());
        }
    }
}
