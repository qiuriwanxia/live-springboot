package org.example.encode;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.example.message.ImMessage;

/**
 * @Author idea
 * @Date created in 9:29 下午 2022/12/22
 */
public class WebsocketEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ImMessage)) {
            super.write(ctx, msg, promise);
            return;
        }
        ImMessage imMsg = (ImMessage) msg;
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(imMsg)));
    }
}
