package org.example.rpc;

import org.apache.dubbo.config.annotation.DubboService;
import org.example.interfaces.IUserRpc;

@DubboService
public class IUserRpcImpl implements IUserRpc {

    @Override
    public String test() {
        System.out.println("this is dubbo test");
        return "success";
    }
}
