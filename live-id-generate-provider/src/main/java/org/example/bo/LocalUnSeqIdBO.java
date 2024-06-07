package org.example.bo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Getter
@Setter
public class LocalUnSeqIdBO {


    /**
     * 分布式id配置主键
     */
    private Integer code;

    /**
     * 当前内存中记录的id值
     */
    private ConcurrentLinkedQueue<Integer> unSeqQueue = new ConcurrentLinkedQueue<>();

    /**
     * 当前id开始的值
     */
    private Long currentStart;

    /**
     * 当前id结束的值
     */
    private Long nextThrshold;

    /**
     * 步长
     */
    private Long step;

}
