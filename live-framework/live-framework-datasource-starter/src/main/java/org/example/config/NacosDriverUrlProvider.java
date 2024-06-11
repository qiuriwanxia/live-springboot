package org.example.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;

import java.util.Properties;

public class NacosDriverUrlProvider implements ShardingSphereDriverURLProvider {

    private final static String NACOS_MARK = "nacos:";
    private final static String FIXED_PREFIX = "jdbc:shardingsphere:";

    @Override
    public boolean accept(String url) {
        return StringUtils.isNotBlank(url) && url.contains(NACOS_MARK);
    }

    @Override
    public byte[] getContent(String url) {
        int nacosPrefix = url.indexOf(FIXED_PREFIX + NACOS_MARK);

        String realUrl = url.substring(nacosPrefix + (FIXED_PREFIX + NACOS_MARK).length(), url.length());

        int serverAddrEndIndex = realUrl.indexOf("?");

        String serverAddr = realUrl.substring(0, serverAddrEndIndex);

        String args = realUrl.substring(serverAddrEndIndex+1, realUrl.length());

        String[] split = args.split("&");

        String dataId = "";
        String group = "";
        String namespace = "";
        String username = "";
        String password = "";

        for (String str : split) {
            int index = str.indexOf("=");
            String substring = str.substring(0, index);
            switch (substring){
                case  "dataId" :
                    dataId = str.substring(index+1,str.length());
                    break;
                case  "group" :
                    group = str.substring(index+1,str.length());
                    break;
                case  "namespace" :
                    namespace = str.substring(index+1,str.length());
                    break;
                case  "username" :
                    username = str.substring(index+1,str.length());
                    break;
                case  "password" :
                    password = str.substring(index+1,str.length());
                    break;
            }
        }

        String content = "";
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", namespace);
            properties.put("username", username);
            properties.put("password", password);
            ConfigService configService = NacosFactory.createConfigService(properties);
            content = configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return content.getBytes();
    }

}
