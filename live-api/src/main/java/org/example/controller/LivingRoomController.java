package org.example.controller;

import jakarta.annotation.Resource;
import org.example.dto.TLivingRoomDTO;
import org.example.service.LivingRoomService;
import org.example.vo.TLivingRoomVo;
import org.example.vo.WebResponseVO;
import org.example.vo.live.req.LivingRoomReqVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/living")
public class LivingRoomController {

    @Resource
    private LivingRoomService livingRoomService;


    @PostMapping("/list")
    public WebResponseVO list(LivingRoomReqVo livingRoomReqVo){

        if (livingRoomReqVo==null
                ||livingRoomReqVo.getPage()<=0
                ||livingRoomReqVo.getPage()>10000){
            return WebResponseVO.error("查询参数有误");
        }


        return WebResponseVO.success(livingRoomService.list(livingRoomReqVo));
    }


    @PostMapping("/startingLiving")
    public WebResponseVO startingLiving(Integer type){

        if (type==null){
            return WebResponseVO.error("请选择需要开播的直播间类型");
        }

        Integer roomId = livingRoomService.startingLiving(type);
        TLivingRoomVo tLivingRoomVo = new TLivingRoomVo();
        tLivingRoomVo.setRoomId(roomId);

        return roomId!=null?WebResponseVO.success(tLivingRoomVo):WebResponseVO.error("开播异常");

    }

    @PostMapping("/closeLiving")
    public WebResponseVO closeLiving(Integer roomId){

        if (roomId==null){
            return WebResponseVO.error("请选择要关闭的直播间");
        }

        boolean closeStatus = livingRoomService.closeLiving(roomId);

        return closeStatus?WebResponseVO.success(roomId):WebResponseVO.error("关播异常");

    }

    @PostMapping("/anchorConfig")
    public WebResponseVO anchorConfig(Integer roomId){
        if (roomId==null){
            return WebResponseVO.error("请选择要关闭的直播间");
        }

        TLivingRoomVo tLivingRoomVo = livingRoomService.anchorConfig(roomId);

        return tLivingRoomVo!=null?WebResponseVO.success(tLivingRoomVo):WebResponseVO.error("查询异常");
    }
}
