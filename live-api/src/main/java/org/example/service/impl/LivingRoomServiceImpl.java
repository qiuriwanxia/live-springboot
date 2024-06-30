package org.example.service.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.example.bo.ConvertBeanBase;
import org.example.dto.TLivingRoomDTO;
import org.example.dto.TLivingRoomRecordDTO;
import org.example.dto.UserDTO;
import org.example.dto.live.req.LivingRoomReqDTO;
import org.example.dto.live.resp.LivingRoomPageRespDTO;
import org.example.dto.live.resp.LivingRoomRespDTO;
import org.example.interfaces.IUserRpc;
import org.example.rpc.LivingRoomRpc;
import org.example.service.LivingRoomService;
import org.example.utils.LiveRequestUtil;
import org.example.vo.TLivingRoomVo;
import org.example.vo.live.req.LivingRoomReqVo;
import org.example.vo.live.resp.LivingRoomPageRespVo;
import org.example.vo.live.resp.LivingRoomRespVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class LivingRoomServiceImpl implements LivingRoomService {

    ConvertBeanBase convertDtoToVo = new ConvertBeanBase<TLivingRoomDTO, TLivingRoomVo>(TLivingRoomDTO.class,TLivingRoomVo.class);
    ConvertBeanBase convertLivingRoomReqVoToLivingRoomReqDTO = new ConvertBeanBase<LivingRoomReqVo, LivingRoomReqDTO>(LivingRoomReqVo.class,LivingRoomReqDTO.class);

    ConvertBeanBase convertLivingRoomRespVoToLivingRoomRespDTO = new ConvertBeanBase<LivingRoomRespVo, LivingRoomRespDTO>(LivingRoomRespVo.class,LivingRoomRespDTO.class);


    @DubboReference
    private LivingRoomRpc livingRoomRpc;
    @DubboReference
    private IUserRpc iUserRpc;

    @Override
    public Integer startingLiving(Integer type) {
        //获取当前登录用户
        Long userId = LiveRequestUtil.getUserId();
        UserDTO byUserId = iUserRpc.getByUserId(userId);

        if (byUserId==null){
            return null;
        }

        TLivingRoomDTO newlivingRoomDTO = new TLivingRoomDTO();

        newlivingRoomDTO.setAnchorId(userId);
        newlivingRoomDTO.setRoomName(byUserId.getNickName()+" 的直播间");
        newlivingRoomDTO.setCovertImg(byUserId.getAvatar());
        newlivingRoomDTO.setStatus(1);
        newlivingRoomDTO.setType(type);
        newlivingRoomDTO.setStartTime(new Date());

        Integer livingRoom = livingRoomRpc.createLivingRoom(newlivingRoomDTO);
        return livingRoom;
    }

    @Override
    public boolean closeLiving(Integer roomId) {
        //获取当前登录用户
        Long userId = LiveRequestUtil.getUserId();
        UserDTO byUserId = iUserRpc.getByUserId(userId);

        if (byUserId==null){
            return false;
        }

        TLivingRoomDTO newlivingRoomDTO = new TLivingRoomDTO();

        newlivingRoomDTO.setAnchorId(userId);
        newlivingRoomDTO.setId(roomId);

        return livingRoomRpc.closeLivingRoom(newlivingRoomDTO);
    }

    @Override
    public TLivingRoomVo anchorConfig(Integer roomId) {
        TLivingRoomDTO tLivingRoomDTO = livingRoomRpc.queryRoomById(roomId);
        if (tLivingRoomDTO==null){
            return null;
        }
        TLivingRoomVo tLivingRoomVo = (TLivingRoomVo)convertDtoToVo.convertToV(tLivingRoomDTO);
        Long anchorId = tLivingRoomDTO.getAnchorId();
        tLivingRoomVo.setRoomId(tLivingRoomDTO.getId());
        tLivingRoomVo.setAnchor(true);
        tLivingRoomVo.setUserId(anchorId);

        UserDTO byUserId = iUserRpc.getByUserId(anchorId);

        tLivingRoomVo.setNickName(byUserId.getNickName());
        tLivingRoomVo.setAvatar(byUserId.getAvatar());

        return tLivingRoomVo;
    }

    @Override
    public LivingRoomPageRespVo list(LivingRoomReqVo livingRoomReqVo) {

        LivingRoomReqDTO livingRoomReqDTO = (LivingRoomReqDTO) convertLivingRoomReqVoToLivingRoomReqDTO.convertToV(livingRoomReqVo);

        List<LivingRoomRespDTO> livingRoomPageRespDTOList =  livingRoomRpc.list(livingRoomReqDTO);

        List<LivingRoomRespVo> livingRoomRespVoList = convertLivingRoomRespVoToLivingRoomRespDTO.convertToKList(livingRoomPageRespDTOList);

        LivingRoomPageRespVo livingRoomPageRespVo = new LivingRoomPageRespVo();

        livingRoomPageRespVo.setList(livingRoomRespVoList);

        livingRoomPageRespVo.setHasNext(livingRoomReqVo.getPage()*livingRoomReqVo.getPageSize()<livingRoomRespVoList.size());

        return livingRoomPageRespVo;
    }

}
