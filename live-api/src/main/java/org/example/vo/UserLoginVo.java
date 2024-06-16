package org.example.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
public class UserLoginVo {

    private boolean loginStatus;

    private String phone;

    private String code;

    private String status;

    private String token;

    private Long userId;

    private String desc;


}
