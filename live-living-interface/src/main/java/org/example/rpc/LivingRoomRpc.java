package org.example.rpc;

import org.example.dto.TLivingRoomDTO;

import org.example.dto.TLivingRoomRecordDTO;
import org.example.dto.live.req.LivingRoomReqDTO;
import org.example.dto.live.resp.LivingRoomRespDTO;

import java.util.List;


public interface LivingRoomRpc {

    Integer createLivingRoom(TLivingRoomDTO tLivingRoomDto);


    boolean closeLivingRoom(TLivingRoomDTO tLivingRoomDto);

    boolean recordLivingRoom(TLivingRoomRecordDTO tLivingRoomRecordDto);

    TLivingRoomDTO queryRoomById(Integer roomID);

    List<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO);
}
