package com.hx.hx.model.dao;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FormDetailDto {
    private String formDataCode;
    private String formCode;
    private String formSubTypeBizCode;
    private String formSubTypeName;

    private String preConsumeName;
    private String applicantEmployeeName;
    private String applicantEmployeeId;

    private String fillEmployeeName;

    private String fillEmployeeId;
    private String departmentBizCode;
    private String departmentName;

    private String legalEntityBizCode;

    private String legalEntityName;
    private String comment;
    private String tradingPartnerBizCode;
    private String tradingPartnerParentBizCode;
    private String tradingPartnerName;
    private Long submittedAt;
    private Object amount;
    private Long firstSubmittedAt;
    private String formStatus;

    private Long approvedAt;

    private Object approvedAmount;
    private Long createdAt;

    private List<Object> applicationExpense;
    private List<Object> validationResult;
    private Object customObject;
    private List<Object> applyRules;
    private  List<Object> procurementDetails;
    private List<String> correlatedReimburseCodes;
    private List<TravelDto> travelRouteList;
    private String preFormCode;
    private Object loanAmount;
    private Object payeeAccount;
    private Long expectRepayDate;
    private List<Object> budgetTransferOut;
    private List<Object> budgetTransferAdd;
    private List<Object> formAssociateUserList;
    private Map<String,Object> detailTotalAmount;
    private String exportComments;
    private String externalVoucherCodes;
    private String externalVoucherStatus;
    private Integer exportStatus;

    private List<Object> tagList;

    private Object travelPartnerInfo;
    private String requestDepartmentBizCode;
    private String requestDepartmentName;
    private List<String> associatedFormCodes;
    private List<Object> associatedForms;
    private List<Object> associatedExternalFormList;

    private Object reportPosition;
    private Object creditScore;
    private Object creditRank;
    private Object headCount;


}
