package org.example.rabbitmq;

import org.example.rabbit.RabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitMQBindConfig {




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
    public DirectExchange imExchange(){
        return new DirectExchange(RabbitMQConfig.IM_EXCHANGE);
    }



    @Bean
    public Queue imBizQueue(){
        return new Queue(RabbitMQConfig.IM_BIZ_QUEUE);
    }

    @Bean
    public Binding queueCommonBingUpdateQueue(
            Exchange imExchange,Queue imBizQueue

    ){
       return BindingBuilder.bind(imBizQueue).to(imExchange).with(RabbitMQConfig.IM_BIZ_KEY).noargs();
    }

}

