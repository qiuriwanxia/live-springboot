package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author idea
 * @Date: Created in 15:54 2023/5/7
 * @Description
 */
@Data
@Getter
@Setter
public class UserDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -4859853766895740696L;
    private Long userId;
    private String nickName;
    private String trueName;
    private String avatar;
    private Integer sex;
    private Integer workCity;
    private Integer bornCity;
    private Date bornDate;
    private Date createTime;
    private Date updateTime;

}