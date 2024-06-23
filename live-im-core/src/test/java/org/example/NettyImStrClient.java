package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.constant.ImMessageMagic;
import org.example.decode.ImByteToMessageDecode;
import org.example.encode.ImMessageToByteEncode;
import org.example.message.ImMessage;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class NettyImStrClient {

    public static void main(String[] args) throws InterruptedException {

        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new ImByteToMessageDecode());
                        nioSocketChannel.pipeline().addLast(new ImMessageToByteEncode());
                    }
                }).connect(new InetSocketAddress(8989)).sync();

        Channel channel = future.channel();

        ImMessage imMessage = new ImMessage();
        imMessage.setMagic(ImMessageMagic.magic);
        imMessage.setCode(101);

        byte[] bytes = "测试消息".getBytes(StandardCharsets.UTF_8);

        imMessage.setLength(bytes.length);
        imMessage.setBytes(bytes);

        channel.writeAndFlush(imMessage);
    }

}
