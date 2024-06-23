package org.example.message;


import org.example.dto.ImMessageBody;

public class ImMessage {

    private short magic;


    private int code;

    private int length;

    private byte[] body;

    public ImMessage() {
    }

    public ImMessage(short magic, int code, int length, byte[] body) {
        this.magic = magic;
        this.code = code;
        this.length = length;
        this.body = body;
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
