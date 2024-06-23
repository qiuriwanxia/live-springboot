package org.example.interfaces;

public interface ImTokenRpc {

    String createToken(Long userId,String appid);


    Long getUserId(String token);

}
