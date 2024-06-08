package org.example.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_user_tag
 */

@Data
@Getter
@Setter
public class TUserTagDTO implements Serializable {
    private Long userId;

    private Long tagInfo01;

    private Long tagInfo02;

    private Long tagInfo03;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}