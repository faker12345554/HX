package com.hx.hx.model.dao;

import lombok.Data;

import java.util.List;

/**
 * @author HiWin11
 */
@Data
public class PreDto {
    private boolean hasNextPage;
    private List<FormDetail> list;
}
