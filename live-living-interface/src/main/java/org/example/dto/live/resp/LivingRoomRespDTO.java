package org.example.dto.live.resp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Data
@Getter
@Setter
public class LivingRoomRespDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6429292639484524451L;
    private Integer id;

    private String roomName;

    private Long anchorId;

    private Integer watchNum;

    private Integer goodNum;

    private String covertImg;


}
