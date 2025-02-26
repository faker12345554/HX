package com.hx.hx.model.dao;

import lombok.Data;

import java.util.List;

@Data
public class FormDetail {
    private String formSubTypeBizCode;
    private String formSubTypeName;
    private String formCode;
    private String formDataCode;
    private String formName;
    private String employeeName;
    private String employeeId;
    private String fillEmployeeName;
    private String fillEmployeeId;
    private List<Object> amount;
    private long submittedAt;
    private String statusText;
    private String formType;
    private List<Object> paymentAmount;
    private String legalEntityBizCode;
    private String legalEntityName;
    private String departmentBizCode;
    private String departmentName;
    private long procInstEndTime;
    private String tradingPartnerName;
    private String tradingPartnerBizCode;
    private Integer exportStatus;
    private String exportComments;
    private String externalVoucherCodes;
    private String externalState;
    private String createdAt;
}
