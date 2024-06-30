package org.example.service;

import org.example.vo.TLivingRoomVo;
import org.example.vo.live.req.LivingRoomReqVo;
import org.example.vo.live.resp.LivingRoomPageRespVo;

public interface LivingRoomService {
    Integer startingLiving(Integer type);

    boolean closeLiving(Integer roomId);

    TLivingRoomVo anchorConfig(Integer roomId);

    LivingRoomPageRespVo list(LivingRoomReqVo livingRoomReqVo);
}
