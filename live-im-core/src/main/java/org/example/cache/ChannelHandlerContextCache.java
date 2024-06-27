package org.example.cache;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ChannelHandlerContextCache {

    static HashMap<Long, ChannelHandlerContext> channelHandlerContextMap = new HashMap<>();

    public static void add(Long userId,ChannelHandlerContext channelHandlerContext){
        channelHandlerContextMap.put(userId,channelHandlerContext);
    }

    public static ChannelHandlerContext get(Long userId){
        return channelHandlerContextMap.get(userId);
    }

    public static void remove(Long userId){
        channelHandlerContextMap.remove(userId);
    }

    public static boolean exist(Long userId){
        return channelHandlerContextMap.containsKey(userId);
    }

}
