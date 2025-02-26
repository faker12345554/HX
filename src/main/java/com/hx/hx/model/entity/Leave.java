package com.hx.hx.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * @author HiWin11
 *
 *
 */
@Data
@Entity
@Table(name="leave")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String workNumber;
    private String type;
    private Date startTime;
    private Date endTime;
    private Double days;
    private String subjectMatter;
    private Integer hours;
    private String formCode;
}
