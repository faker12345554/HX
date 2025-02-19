package com.hx.hx.service;

import com.hx.hx.mapper.LeaveMapper;
import com.hx.hx.model.entity.Leave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaveService {
    @Autowired
    private LeaveMapper leavemapper;

    public Leave setLeaveMapper(Leave leave) {
        leave= leavemapper.save(leave);
        return leave;
    }
}
