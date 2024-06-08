package org.example.rpc;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.constant.UserTagsEnum;
import org.example.interfaces.IUserTagRpc;
import org.example.service.TUserTagService;

@DubboService
public class IUserTagRpcImpl implements IUserTagRpc {

    @Resource
    private TUserTagService tUserTagService;

    @Override
    public boolean setTag(Long userId, UserTagsEnum userTagsEnum) {
        return tUserTagService.setTag(userId,userTagsEnum);
    }

    @Override
    public boolean cancelTag(Long userId, UserTagsEnum userTagsEnum) {
        return tUserTagService.cancelTag(userId,userTagsEnum);
    }

    @Override
    public boolean containTag(Long userId, UserTagsEnum userTagsEnum) {
        return tUserTagService.containTag(userId,userTagsEnum);
    }
}
