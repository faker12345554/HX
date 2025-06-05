package com.hx.hx.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TravelDto {
    private String travelRouteCode;
    private List<String> expenseCodeList;
    private List<String> applicationExpenseCodeList;
    private Object travelCity;

    private timeDto travelTime;
    private String tripWay;
    private double travelDays;
    private Object travelPartnerInfo;
    private Object customObject;
    private Integer travelTimeOffset;

}
