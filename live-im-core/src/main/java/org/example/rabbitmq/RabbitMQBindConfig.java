package org.example.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import jakarta.annotation.Resource;
import org.example.rabbit.RabbitMQConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RabbitMQBindConfig {

    @Bean
    public DirectExchange imExchange(){
        return new DirectExchange(RabbitMQConfig.IM_EXCHANGE);
    }

    @Bean
    public DirectExchange imDieExchange(){
        return new DirectExchange(RabbitMQConfig.IM_DIE_EXCHANGE);
    }



    @Bean
    public Queue imBizQueue(){
        HashMap<String, Object> argumentsMap
                = new HashMap<>();

        argumentsMap.put("x-dead-letter-exchange",RabbitMQConfig.IM_DIE_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        argumentsMap.put("x-dead-letter-routing-key", RabbitMQConfig.IM_BIZ_DIE_KEY);


        return  QueueBuilder.durable(RabbitMQConfig.IM_BIZ_QUEUE).withArguments(argumentsMap).build();
    }

    @Bean
    public Queue imDieBizQueue(){
        return new Queue(RabbitMQConfig.IM_BIZ_DIE_QUEUE);
    }

    @Bean
    public Binding bizBingUpdateQueue(
            Exchange imExchange,Queue imBizQueue

    ){
       return BindingBuilder.bind(imBizQueue).to(imExchange).with(RabbitMQConfig.IM_BIZ_KEY).noargs();
    }

    @Bean
    public Binding bizDieBingUpdateQueue(
            Exchange imDieExchange,Queue imDieBizQueue
    ){
        return BindingBuilder.bind(imDieBizQueue).to(imDieExchange).with(RabbitMQConfig.IM_BIZ_DIE_KEY).noargs();
    }

    @RabbitListener()
    public void init(){

    };
}

