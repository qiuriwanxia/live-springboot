package org.example.util;

import ch.qos.logback.core.PropertyDefinerBase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 保证每个docker容器的日志挂载目录唯一性
 *
 * @Author idea
 * @Date: Created in 15:57 2023/6/3
 * @Description
 */
public class IpLogConversionRule extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return this.getLogIndex();
    }

    private String getLogIndex() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000));
    }
}
