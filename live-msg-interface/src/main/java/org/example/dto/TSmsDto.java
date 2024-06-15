package org.example.dto;



import java.io.Serializable;
import java.util.Date;


public class TSmsDto implements Serializable {
    private Long id;

    private Integer code;

    private String phone;

    private Date sendTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}