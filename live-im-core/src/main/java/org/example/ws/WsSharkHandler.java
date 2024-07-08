package org.example.ws;

import cn.hutool.http.HttpUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.example.enums.ImMessageEnum;
import org.example.message.ImMessage;
import org.example.rpc.interfaces.ImTokenRpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@ChannelHandler.Sharable
@Slf4j
public class WsSharkHandler extends ChannelInboundHandlerAdapter {

    @Value("${im.ws.port}")
    private int port;

    @Value("im.server.ip")
    private String serverIp;


    @DubboReference(check = false)
    private ImTokenRpc imTokenRpc;


    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        
        if (msg instanceof FullHttpRequest){
            hanldeHttpRequest(ctx,(FullHttpRequest) msg);
        }
        
        if (msg instanceof CloseWebSocketFrame){
            webSocketServerHandshaker.close(ctx.channel(),(CloseWebSocketFrame) ((WebSocketFrame)msg).retain());
        }
        
        ctx.fireChannelRead(msg);
    }

    private void hanldeHttpRequest(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String webSocketUrl = "ws://"+serverIp+":"+port;
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(webSocketUrl, null, false);
        String uri = msg.uri();
        Map<String, String> urlParamMap = HttpUtil.decodeParamMap(uri, StandardCharsets.UTF_8);
        String token = urlParamMap.get("token");
        String userId =urlParamMap.get("userId");
        Long rpcUserId = imTokenRpc.getUserId(token);
        if (rpcUserId==null||!rpcUserId.toString().equals(userId)){
            log.error("ws token 校验不通过");
            ctx.close();
            return;
        }

        WebSocketServerHandshaker webSocketServerHandshaker1 = webSocketServerHandshakerFactory.newHandshaker(msg);

        if (webSocketServerHandshaker1==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            return;
        }

        ChannelFuture handshake = webSocketServerHandshaker1.handshake(ctx.channel(), msg);

        if (handshake.isSuccess()){
            ImMessage imMessage = new ImMessage();
            imMessage.setCode(ImMessageEnum.WS_SHARD_MESSAGE.getCode());
            imMessage.setBody("success".getBytes());
            handshake.channel().writeAndFlush(imMessage);
        }

    }
}
