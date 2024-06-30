package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.dto.TLivingRoomDTO;
import org.example.dto.TLivingRoomRecordDTO;
import org.example.dto.live.req.LivingRoomReqDTO;
import org.example.dto.live.resp.LivingRoomRespDTO;
import org.example.service.TLivingRoomRecordService;
import org.example.service.TLivingRoomService;

import java.util.List;


@DubboService
public class LivingRoomRpcImpl implements LivingRoomRpc{

    @Resource
    private TLivingRoomService tLivingRoomService;


    @Resource
    private TLivingRoomRecordService gettLivingRoomRecordService;

    @Override
    public Integer createLivingRoom(TLivingRoomDTO tLivingRoomDto) {
        return tLivingRoomService.createLivingRoom(tLivingRoomDto);
    }

    @Override
    public boolean closeLivingRoom(TLivingRoomDTO tLivingRoomDto) {
        return tLivingRoomService.closeLivingRoom(tLivingRoomDto);
    }

    @Override
    public boolean recordLivingRoom(TLivingRoomRecordDTO tLivingRoomRecordDto) {
        return gettLivingRoomRecordService.recordLivingRoom(tLivingRoomRecordDto);
    }

    @Override
    public TLivingRoomDTO queryRoomById(Integer roomID) {
        return tLivingRoomService.queryRoomById(roomID);
    }

    @Override
    public List<LivingRoomRespDTO> list(LivingRoomReqDTO livingRoomReqDTO) {
        return tLivingRoomService.queryList(livingRoomReqDTO);
    }
}
