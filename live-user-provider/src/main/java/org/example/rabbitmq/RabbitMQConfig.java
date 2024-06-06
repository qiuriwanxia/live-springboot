package org.example.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitMQConfig {

    public static final String COMMON_EXCHANGE = "cm_ex";
    public static final String TTL_EXCHANGE = "ttl_ex";


    //定义队列名

    public static final String USER_UPDATE_QUEUE = "user-update-cache-queue";

    public static final String USER_UPDATE_TTL_QUEUE = "user-update-cache-ttl-queue";

    public static final String USER_UPDATE_QUEUE_TTL_KEY = "user-update-cache-ttl";

    public static final String USER_UPDATE_QUEUE_KEY = "user-update-cache";


    //创建队列
    /**
     * 1. 配置队列
     * 2. 队列名为 queue
     * 3. true 表示: 持久化 (不填，默认为true,默认持久化)
     * durable： 队列是否持久化。 队列默认是存放到内存中的，rabbitmq 重启则丢失，
     * 若想重启之后还存在则队列要持久化，
     * 保存到 Erlang 自带的 Mnesia 数据库中，当 rabbitmq 重启之后会读取该数据库
     * @return
     */

    @Bean
    public DirectExchange commonExchange(){
        return new DirectExchange(COMMON_EXCHANGE);
    }

    @Bean
    public DirectExchange ttlExchange(){
        return new DirectExchange(TTL_EXCHANGE);
    }

    @Bean
    public Queue updateQueue(){
        HashMap<String, Object> argumentsMap
                = new HashMap<>();

        argumentsMap.put("x-dead-letter-exchange",TTL_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        argumentsMap.put("x-dead-letter-routing-key", USER_UPDATE_QUEUE_TTL_KEY);
        //设置过期时间 40s,单位是ms，可以在消费者正常队列处设置，也可以在生产者发送处设置]
        argumentsMap.put("x-message-ttl", 3000);


        return QueueBuilder.durable(USER_UPDATE_QUEUE)
                .withArguments(argumentsMap).build();
    }

    @Bean
    public Queue updateTtlQueue(){
        return new Queue(USER_UPDATE_TTL_QUEUE);
    }

    @Bean
    public Binding queueCommonBingUpdateQueue(
            Exchange commonExchange,Queue updateQueue

    ){
       return BindingBuilder.bind(updateQueue).to(commonExchange).with(USER_UPDATE_QUEUE_KEY).noargs();
    }

    @Bean
    public Binding queuettlBingUpdateQueue(
            Exchange ttlExchange,Queue updateTtlQueue

    ){
        return BindingBuilder.bind(updateTtlQueue).to(ttlExchange).with(USER_UPDATE_QUEUE_TTL_KEY).noargs();
    }
}

