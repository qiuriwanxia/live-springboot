package org.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.constant.UserTagsEnum;
import org.example.dao.pojo.TUserTag;

/**
* @author sz
* @description 针对表【t_user_tag(用户标签记录)】的数据库操作Service
* @createDate 2024-06-07 20:15:13
*/
public interface TUserTagService extends IService<TUserTag> {

    boolean setTag(Long userId, UserTagsEnum userTagsEnum);

    boolean cancelTag(Long userId, UserTagsEnum userTagsEnum);

    boolean containTag(Long userId, UserTagsEnum userTagsEnum);
}
