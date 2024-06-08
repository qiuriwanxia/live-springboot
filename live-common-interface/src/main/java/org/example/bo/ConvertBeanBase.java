package org.example.bo;

import org.example.util.ConvertBeanUtils;

import java.util.List;

public class ConvertBeanBase<K,V> {

    private Class<K> kClass;
    private Class<V> vClass;


    public ConvertBeanBase(Class<K> kClass, Class<V> vClass) {
        this.kClass = kClass;
        this.vClass = vClass;
    }

    public K convertToK(V v) {
        return ConvertBeanUtils.convert(v, kClass);
    }

    public List<K> convertToKList(List<V> vList) {
        return ConvertBeanUtils.convertList(vList, kClass);
    }

    public List<V> convertToVList(List<K> kList) {
        return ConvertBeanUtils.convertList(kList, vClass);
    }

    public V convertToV(K k) {
        return ConvertBeanUtils.convert(k, vClass);
    }


}
