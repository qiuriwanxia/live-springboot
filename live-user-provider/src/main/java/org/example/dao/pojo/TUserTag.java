package org.example.dao.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_user_tag
 */
@TableName(value ="t_user_tag")
@Data
public class TUserTag implements Serializable {
    private Long userId;

    private Long tagInfo01;

    private Long tagInfo02;

    private Long tagInfo03;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}