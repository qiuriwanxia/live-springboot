package org.example.cluster;

import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.rpc.cluster.Directory;
import org.example.invoker.ImRouterInvoker;

public class ImRouterCluster implements Cluster {

    @Override
    public <T> Invoker<T> join(Directory<T> directory, boolean buildFilterChain) throws RpcException {
        return new ImRouterInvoker<>(directory);
    }

}
