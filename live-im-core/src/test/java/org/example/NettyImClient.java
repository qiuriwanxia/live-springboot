package org.example;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.Resource;
import org.example.constant.ImMessageMagic;
import org.example.decode.ImByteToMessageDecode;
import org.example.dto.ImMessageBody;
import org.example.encode.ImMessageToByteEncode;
import org.example.enums.ImMessageEnum;
import org.example.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class NettyImClient {

    @Resource
    private ImTokenRpc imTokenRpc;

    @Test
    public void test1() throws InterruptedException {

        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        pipeline.addFirst(new ImByteToMessageDecode());
                        pipeline.addLast( new ImMessageToByteEncode());

                    }
                }).connect(new InetSocketAddress(9191)).sync();

        Channel channel = future.channel();

        ImMessage imMessage = new ImMessage();
        imMessage.setMagic(ImMessageMagic.magic);
        imMessage.setCode(ImMessageEnum.LOGIN_MESSAGE.getCode());

        //获取用户token
        String token = imTokenRpc.createToken(10086L, "1");

        ImMessageBody imMessageBody = new ImMessageBody();
        imMessageBody.setAppid("1");
        imMessageBody.setToken(token);
        imMessageBody.setData("");


        byte[] jsonBytes = JSON.toJSONBytes(imMessageBody);
        imMessage.setLength(jsonBytes.length);
        imMessage.setBody(jsonBytes);

        channel.writeAndFlush(imMessage);


        TimeUnit.SECONDS.sleep(10);

        channel.close();
    }

}
