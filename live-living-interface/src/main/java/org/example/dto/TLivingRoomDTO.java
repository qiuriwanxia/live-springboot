package org.example.dto;




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
public class TLivingRoomDTO implements Serializable {
    private Integer id;

    private Long anchorId;

    private Integer type;

    private Integer status;

    private String roomName;

    private String covertImg;

    private Integer watchNum;

    private Integer goodNum;

    private Date startTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}