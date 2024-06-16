package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Getter
@Setter
public class TUserPhoneDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -5930585114229464425L;

    private Long id;

    private String phone;

    private Long userId;

    private int status;

    private Date createTime;

    private Date updateTime;

}
