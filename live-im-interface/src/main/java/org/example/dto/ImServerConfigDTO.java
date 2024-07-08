package org.example.dto;

import java.io.Serial;
import java.io.Serializable;

public class ImServerConfigDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1869529301865461526L;


    private String wsImServerAddress;


    private String token;

    public ImServerConfigDTO() {
    }

    public ImServerConfigDTO(String wsImServerAddress, String token) {
        this.wsImServerAddress = wsImServerAddress;
        this.token = token;
    }

    public String getWsImServerAddress() {
        return wsImServerAddress;
    }

    public void setWsImServerAddress(String wsImServerAddress) {
        this.wsImServerAddress = wsImServerAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
