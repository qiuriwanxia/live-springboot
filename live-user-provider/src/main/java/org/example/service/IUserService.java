package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.dao.pojo.UserPO;
import org.example.dto.UserDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IUserService extends IService<UserPO> {
    UserDTO getByUserId(Long userId);

    boolean updateByUserId(UserDTO userDTO);

    boolean insertUserId(UserDTO userDTO);

    Map<Long,UserDTO> batchQueryUserByIdList(List<Long> idList);
}
