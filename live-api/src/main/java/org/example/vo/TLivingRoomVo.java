package org.example.vo;




import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName t_living_room
 */

@Data
@Getter
@Setter
public class TLivingRoomVo implements Serializable {
    private Integer roomId;

    private Long anchorId;

    private boolean anchor;

    private String avatar;

    private Long userId;

    private Integer type;

    private Integer status;

    private String roomName;

    private String nickName;

    private String covertImg;

    private Integer watchNum;

    private Integer goodNum;

    private Date startTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}