package com.hx.hx.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author HiWin11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PreDto {
    private boolean hasNextPage;
    private List<FormDetail> list;
}
