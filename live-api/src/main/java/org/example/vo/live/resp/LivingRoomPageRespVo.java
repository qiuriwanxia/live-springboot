package org.example.vo.live.resp;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class LivingRoomPageRespVo {

    private List<LivingRoomRespVo> list;

    private boolean hasNext;


}
