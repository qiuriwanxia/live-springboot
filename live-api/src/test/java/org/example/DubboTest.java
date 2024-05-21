package org.example;

import org.apache.dubbo.config.*;
import org.example.dto.UserDTO;
import org.example.interfaces.IUserRpc;

public class DubboTest {

    RegistryConfig registryConfig;
    ApplicationConfig applicationConfig;


    public void init(){
        registryConfig = new RegistryConfig();
        registryConfig.setAddress("nacos://127.0.0.1:8848?namespace=05e9183f-7ab2-45fb-8a8e-ed4e4caf2e63");

        applicationConfig = new ApplicationConfig();
        applicationConfig.setRegistry(registryConfig);
        applicationConfig.setName("live-api");

        ReferenceConfig<IUserRpc> iUserRpcReferenceConfig = new ReferenceConfig<>();

        iUserRpcReferenceConfig.setInterface(IUserRpc.class);
        iUserRpcReferenceConfig.setLoadbalance("random");

        iUserRpcReferenceConfig.setRegistry(registryConfig);
        iUserRpcReferenceConfig.setApplication(applicationConfig);
    }

    public void initProvider(){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(9090);
        protocolConfig.setName("dubbo");

        ServiceConfig<IUserRpc> iUserRpcServiceConfig = new ServiceConfig<>();

        iUserRpcServiceConfig.setInterface(IUserRpc.class);
        iUserRpcServiceConfig.setRegistry(registryConfig);
        iUserRpcServiceConfig.setApplication(applicationConfig);

        //暴露服务
        iUserRpcServiceConfig.export();
    }

    public void initConsumer(){
        ReferenceConfig<IUserRpc> iUserRpcReferenceConfig = new ReferenceConfig<>();
        iUserRpcReferenceConfig.setRegistry(registryConfig);
        iUserRpcReferenceConfig.setApplication(applicationConfig);
        iUserRpcReferenceConfig.setLoadbalance("random");
        iUserRpcReferenceConfig.setInterface(IUserRpc.class);
        IUserRpc iUserRpc = iUserRpcReferenceConfig.get();
        String test = iUserRpc.test();
        System.out.println("test = " + test);
    }

    public static void main(String[] args) {
        DubboTest dubboTest = new DubboTest();
        dubboTest.init();
        dubboTest.initProvider();
        dubboTest.initConsumer();

    }

}
