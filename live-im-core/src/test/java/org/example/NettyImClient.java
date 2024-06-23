package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.constant.ImMessageMagic;
import org.example.decode.ImByteToMessageDecode;
import org.example.encode.ImMessageToByteEncode;
import org.example.message.ImMessage;

import java.net.InetSocketAddress;

public class NettyImClient {

    public static void main(String[] args) throws InterruptedException {

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
                }).connect(new InetSocketAddress(8989)).sync();

        Channel channel = future.channel();

        ImMessage imMessage = new ImMessage();
        imMessage.setMagic(ImMessageMagic.magic);
        imMessage.setCode(12);

        byte[] bytes = "你好".getBytes();

        imMessage.setLength(bytes.length);
        imMessage.setBody(bytes);

        channel.writeAndFlush(imMessage);
    }

}
