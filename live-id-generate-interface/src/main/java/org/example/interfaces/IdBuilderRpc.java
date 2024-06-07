package org.example.interfaces;

public interface IdBuilderRpc {

    /**
     * 根据本地步长度来生成唯一 id(区间性递增)
     *
     * @return
     */
    Integer increaseSeqId(int code);

    /**
     * 生成的是非连续性 id
     *
     * @param code
     * @return
     */
    Integer increaseUnSeqId(int code);
    
    /**
     * 根据本地步长度来生成唯一 id(区间性递增)
     *
     * @param code
     * @return
     */
    String increaseSeqStrId(int code);

}
