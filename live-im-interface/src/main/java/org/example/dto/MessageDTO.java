package org.example.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class MessageDTO implements Serializable {


    @Serial
    private static final long serialVersionUID = -6220558040255143867L;


    private Long userId;

    private Long rUserId;

    private Integer type;

    private String content;

    private Date createtime;

    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getrUserId() {
        return rUserId;
    }

    public void setrUserId(Long rUserId) {
        this.rUserId = rUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
