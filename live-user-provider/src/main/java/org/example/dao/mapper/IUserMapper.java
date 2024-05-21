package org.example.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.dao.pojo.UserPO;

@Mapper
public interface IUserMapper extends BaseMapper<UserPO> {
}
