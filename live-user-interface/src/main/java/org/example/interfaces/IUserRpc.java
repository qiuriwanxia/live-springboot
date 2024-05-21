package org.example.interfaces;

import org.example.dto.UserDTO;

public interface IUserRpc {
    String test();

    UserDTO getByUserId(Long userId);

    boolean updateByUserId(UserDTO userDTO);

    boolean insertUserId(UserDTO userDTO);

}
