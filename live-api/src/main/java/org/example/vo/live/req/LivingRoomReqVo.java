package org.example.vo.live.req;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LivingRoomReqVo {

    private Integer type;

    private int page;

    private int pageSize;


}
