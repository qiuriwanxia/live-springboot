package org.example.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.decode.ImByteToMessageDecode;
import org.example.encode.ImMessageToByteEncode;
import org.example.handler.ImMessageHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Configuration
public class NettyImServerApplication implements InitializingBean {


    @Value("${netty.port}")
    public int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void startServer(int port) throws InterruptedException {
        setPort(port);

        NioEventLoopGroup boosEventLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workEvenloopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        ChannelFuture channelFuture = new ServerBootstrap()
                .group(boosEventLoopGroup, workEvenloopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ImByteToMessageDecode());
                        pipeline.addLast( new ImMessageToByteEncode());
                        pipeline.addLast(new ImMessageHandler());
                    }
                }).bind(getPort()).sync();


        channelFuture.channel().closeFuture().sync();

        //jvm关闭时关闭netty
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            boosEventLoopGroup.shutdownGracefully();
            workEvenloopGroup.shutdownGracefully();
        }));
    }


//    public static void main(String[] args) {
//
//
//        NettyImServerApplication nettyImServerApplication = new NettyImServerApplication();
//        try {
//            nettyImServerApplication.startServer(8989);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread thread = new Thread(
                ()->{
                    try {
                        log.info("正在启动 im netty 端口 ： {}",port);
                        this.startServer(port);
                    } catch (InterruptedException e) {
                        log.error("im netty启动异常 : {}",e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
        );

        thread.setName("im-"+ ThreadLocalRandom.current().nextInt(10));

        thread.start();
    }
}
