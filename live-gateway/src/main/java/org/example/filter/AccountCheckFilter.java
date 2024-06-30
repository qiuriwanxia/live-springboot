package org.example.filter;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.logging.log4j.util.Strings;
import org.example.constant.GateWayHeadEnum;
import org.example.interfaces.IAccountTokenRPC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Configuration
public class AccountCheckFilter implements GlobalFilter {

    @Value("${gateway.whiltlist}")
    String[] whiltList;

    @DubboReference
    private IAccountTokenRPC iAccountTokenRPC;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest  request = exchange.getRequest();

        String path = request.getURI().getPath();

        for (String wl : whiltList) {
            if (path.contains(wl)){
                //直接放行
                return chain.filter(exchange);
            }
        }

        //如果不在白名单，检查cookie

        List<HttpCookie> tk = request.getCookies().get("tk");

        if (tk==null||tk.isEmpty()){
            return Mono.empty();
        }

        HttpCookie httpCookie = tk.get(0);

        String cookieValue = httpCookie.getValue();

        if (Strings.isEmpty(cookieValue)){
            //拦截
            return Mono.empty();
        }

        Long userIdByToken = iAccountTokenRPC.getUserIdByToken(cookieValue);

        if (null==userIdByToken){
            //拦截
            return Mono.empty();
        }

        //透传userId
        ServerHttpRequest.Builder serverRequestMutate = request.mutate();

        serverRequestMutate.header(GateWayHeadEnum.USER_LOGIN_ID.getName(),userIdByToken.toString());


        return chain.filter(exchange.mutate().request(serverRequestMutate.build()).build());
    }


}
