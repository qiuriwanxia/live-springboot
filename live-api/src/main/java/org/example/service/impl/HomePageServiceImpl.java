package org.example.service.impl;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.constant.UserTagsEnum;
import org.example.dto.UserDTO;
import org.example.interfaces.IUserRpc;
import org.example.interfaces.IUserTagRpc;
import org.example.service.HomePageService;
import org.example.vo.HomePageVo;
import org.springframework.stereotype.Service;

@Service
public class HomePageServiceImpl implements HomePageService {

    @DubboReference
    private IUserTagRpc iUserTagRpc;
    @DubboReference
    private IUserRpc iUserRpc;

    @Override
    public HomePageVo initPage(Long userId) {
        UserDTO byUserId = iUserRpc.getByUserId(userId);
        if (byUserId!=null){
            HomePageVo homePageVo = new HomePageVo();
            homePageVo.setAvatar(byUserId.getAvatar());
            homePageVo.setUserId(userId);
            homePageVo.setLoginStatus(true);
            homePageVo.setNickName(byUserId.getNickName());
            homePageVo.setShowStartLivingBtn(iUserTagRpc.containTag(userId, UserTagsEnum.IS_VIP));
            return homePageVo;
        }
        return null;
    }
}
