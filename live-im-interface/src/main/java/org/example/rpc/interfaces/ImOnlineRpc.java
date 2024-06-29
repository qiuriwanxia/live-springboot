package org.example.rpc.interfaces;

public interface ImOnlineRpc {

    /**
     * 判断用户是否在线
     * @param userId
     * @return
     */
    boolean isOnine(Long userId);

}
