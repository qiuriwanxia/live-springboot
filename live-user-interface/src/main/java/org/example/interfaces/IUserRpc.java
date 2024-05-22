package org.example.interfaces;

import org.example.dto.UserDTO;

import java.util.List;
import java.util.Map;

public interface IUserRpc {
    String test();

    UserDTO getByUserId(Long userId);

    boolean updateByUserId(UserDTO userDTO);

    boolean insertUserId(UserDTO userDTO);

    Map<Long,UserDTO> batchQueryUserByIdList(List<Long> idList);
}
