package org.example.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.bo.ConvertBeanBase;
import org.example.constant.MsgSendResultEnum;
import org.example.dao.mapper.TSmsMapper;

import org.example.dao.pojo.TSms;
import org.example.dto.TSmsDto;
import org.example.key.LiveMsgCacheKey;
import org.example.service.TSmsService;
import org.example.util.CommonExecutorPool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
* @author sz
* @description 针对表【t_sms】的数据库操作Service实现
* @createDate 2024-06-15 21:00:31
*/
@Service
@Slf4j
public class TSmsServiceImpl extends ServiceImpl<TSmsMapper, TSms>
    implements TSmsService {


    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private LiveMsgCacheKey liveMsgCacheKey;


    ConvertBeanBase convertBeanBase = new ConvertBeanBase<TSms, TSmsDto>(TSms.class,TSmsDto.class);


    @Override
    public MsgSendResultEnum sendMsg(String phone) {

        //判断手机号
        if (Strings.isNullOrEmpty(phone)){
            return MsgSendResultEnum.MSG_PARAM_ERROR;
        }

        String key = liveMsgCacheKey.buildMsgCodeKey(phone);
        Object keyObject = (Object) redisTemplate.opsForValue().get(key);
        if (keyObject!=null){
            return MsgSendResultEnum.SEND_FAIL;
        }


        //生成六位数验证码
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);

        CommonExecutorPool.msgExecutorPool.execute(()-> {
                    //模拟发送验证码
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    log.info("{} 发送验证码成功 code : {}", phone, code);
                });

        //存入缓存
        redisTemplate.opsForValue().set(key,code,60,TimeUnit.SECONDS);

        TSms tSms = buildTSms(phone, code);

        this.recordMsgLog(tSms);

        return MsgSendResultEnum.SEND_SUCCESS;
    }

    @Override
    public MsgSendResultEnum checkMsgCode(String phone, String code) {
        //检查当前手机号与验证码
        if (Strings.isNullOrEmpty(phone)||Strings.isNullOrEmpty(code)){
            return MsgSendResultEnum.CHECK_ERROR;
        }

        String key = liveMsgCacheKey.buildMsgCodeKey(phone);

        String msgCode = (String) redisTemplate.opsForValue().get(key);

        if (code.equals(msgCode)){
            return MsgSendResultEnum.CHECK_SUCCESS;
        }

        return MsgSendResultEnum.CHECK_ERROR;
    }

    @Override
    public boolean recordMsgLog(TSms tSms) {
        return this.baseMapper.insert(tSms)>0;
    }

    private static TSms buildTSms(String phone, int code) {
        TSms tSms = new TSms();
        tSms.setCode(code);
        tSms.setPhone(phone);
        tSms.setSendTime(new Date());
        tSms.setUpdateTime(new Date());
        return tSms;
    }
}




