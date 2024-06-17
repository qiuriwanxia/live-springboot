package org.example.interfaces;

public interface IAccountTokenRPC {

    /**
     * 创建一个登录 token
     *
     * @param userId
     * @return
     */
    String createAndSaveLoginToken(Long userId);
    /**
     * 校验用户 token
     *
     * @param tokenKey
     * @return
     */
    Long getUserIdByToken(String tokenKey);

}
