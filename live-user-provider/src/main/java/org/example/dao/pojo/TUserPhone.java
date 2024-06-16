package org.example.dao.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_user_phone
 */
@TableName(value ="t_user_phone")
@Data
public class TUserPhone {
    private Long id;

    private String phone;

    private Long userId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}