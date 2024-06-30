package org.example.vo.live.resp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LivingRoomRespVo {

    private Integer id;

    private String roomName;

    private Long anchorId;

    private Integer watchNum;

    private Integer goodNum;

    private String covertImg;


}
