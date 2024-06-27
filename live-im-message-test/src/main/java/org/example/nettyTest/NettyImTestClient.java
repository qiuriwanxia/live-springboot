package org.example.nettyTest;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.example.constant.ImMessageMagic;

import org.example.dto.ImMessageBody;

import org.example.dto.MessageDTO;
import org.example.enums.ImBizMessageEnum;
import org.example.enums.ImMessageEnum;
import org.example.interfaces.ImTokenRpc;
import org.example.message.ImMessage;
import org.example.nettyTest.decode.ImByteToMessageDecode;
import org.example.nettyTest.encode.ImMessageToByteEncode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableDubbo
@EnableDiscoveryClient
public class NettyImTestClient implements CommandLineRunner {

    @DubboReference
    private ImTokenRpc imTokenRpc;

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        springApplication.run(NettyImTestClient.class);

    }


    @Override
    public void run(String... args) throws Exception {
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
        imMessage.setCode(ImMessageEnum.BIZ_MESSAGE.getCode());

        //获取用户token
        String token = imTokenRpc.createToken(10000L, "1");

        ImMessageBody imMessageBody = new ImMessageBody();
        imMessageBody.setAppid("1");
        imMessageBody.setToken(token);
        imMessageBody.setMessageType(ImBizMessageEnum.LIVING_ROOM_IM_CHAT_BIZ.getCode());

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setUserId(10000L);
        messageDTO.setrUserId(10086L);
        messageDTO.setContent("这是10000发给10086的消息");

        imMessageBody.setData(JSON.toJSONString(messageDTO));


        byte[] jsonBytes = JSON.toJSONBytes(imMessageBody);
        imMessage.setLength(jsonBytes.length);
        imMessage.setBody(jsonBytes);

        channel.writeAndFlush(imMessage);


        TimeUnit.SECONDS.sleep(1000);

        channel.close();
    }
}
