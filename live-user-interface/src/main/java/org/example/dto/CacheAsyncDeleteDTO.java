package org.example.dto;


import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.constant.CacheAsyncDeleteCode;

@Data
@Getter
@Setter
public class CacheAsyncDeleteDTO {

    private int code;

    private String json;

    public CacheAsyncDeleteDTO(int code, String json) {
        this.code = code;
        this.json = json;
    }

    public static byte[] buildThenTobytes(int code,String jsonStr){
        return JSON.toJSONBytes(new CacheAsyncDeleteDTO(code, jsonStr));
    }
}
