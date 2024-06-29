package org.example.rpc.interfaces;

public interface ImTokenRpc {

    String createToken(Long userId,String appid);


    Long getUserId(String token);

}
