package org.example.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.constant.UserTagsEnum;
import org.example.dao.pojo.TUserTag;

/**
* @author sz
* @description 针对表【t_user_tag(用户标签记录)】的数据库操作Mapper
* @createDate 2024-06-07 20:15:13
* @Entity generator.domain.TUserTag
*/
@Mapper
public interface TUserTagMapper extends BaseMapper<TUserTag> {

    int updateTag(@Param("userId") Long userId, @Param("userTagsEnum") UserTagsEnum userTagsEnum);

    @Update("        update t_user_tag set ${userTagsEnum.tableName} = ${userTagsEnum.tableName} &~ ${userTagsEnum.tag}\n" +
            "        where user_id = #{userId}")
    int cancelTag(@Param("userId") Long userId, @Param("userTagsEnum") UserTagsEnum userTagsEnum);

    TUserTag selectByUserId(@Param("userId") Long userId);
}




