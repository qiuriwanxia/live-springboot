package org.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.constant.MsgSendResultEnum;
import org.example.dao.pojo.TSms;
import org.example.dto.TSmsDto;

/**
* @author sz
* @description 针对表【t_sms】的数据库操作Service
* @createDate 2024-06-15 21:00:31
*/
public interface TSmsService extends IService<TSms> {

    MsgSendResultEnum sendMsg(String phone);

    MsgSendResultEnum checkMsgCode(String phone,String code);

    boolean recordMsgLog(TSms tSms);
}
