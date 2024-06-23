package org.example.service;

public interface ImTokenService {

    String createToken(Long userId,String appid);


    Long getUserId(String token);

}
