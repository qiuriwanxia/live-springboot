package org.example.invoker;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;

import java.util.List;

@Slf4j
public class ImRouterInvoker<T> extends AbstractClusterInvoker<T> {

    public ImRouterInvoker(Directory directory) {
        super(directory);
    }

    @Override
    protected Result doInvoke(Invocation invocation, List list, LoadBalance loadbalance) throws RpcException {

        String ip = (String) RpcContext.getContext().get("ip");
        log.info("消息路由ip {}",ip);

        List<Invoker<T>> invokerList = list(invocation);

        Invoker<T> tInvoker = invokerList.stream().filter(i -> {
            String serverIP = i.getUrl().getHost() ;
            return serverIP.equals(ip);
        }).findFirst().orElse(null);

        if (tInvoker==null){
            log.error("消息无法路由 ip:{}",ip);
            throw new RuntimeException("error no match router invoker");
        }

        return tInvoker.invoke(invocation);

    }
}
