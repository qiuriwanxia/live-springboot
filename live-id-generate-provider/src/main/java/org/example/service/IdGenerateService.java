package org.example.service;

public interface IdGenerateService {

    /**
     * 生成有序 id
     *
     * @param code
     * @return
     */
    Integer getSeqId(Integer code);
    /**
     * 生成无序 id
     *
     * @param code
     * @return
     */
    Integer getUnSeqId(Integer code);

}
