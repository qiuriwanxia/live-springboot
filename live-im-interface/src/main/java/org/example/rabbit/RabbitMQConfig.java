package org.example.rabbit;



import javax.naming.Binding;
import java.util.Queue;


public class RabbitMQConfig {

    public static final String IM_EXCHANGE = "im_ex";
    public static final String IM_DIE_EXCHANGE = "im_die_ex";



    //定义队列名

    public static final String IM_BIZ_QUEUE = "im_ex-queue";

    public static final String IM_BIZ_DIE_QUEUE = "im_die_ex-queue";


    public static final String IM_BIZ_KEY = "im-biz-key";
    public static final String IM_BIZ_DIE_KEY = "im-biz-die-key";




}

