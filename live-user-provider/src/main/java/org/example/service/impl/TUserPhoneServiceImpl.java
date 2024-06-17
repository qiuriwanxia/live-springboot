package org.example.service.impl;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.example.bo.ConvertBeanBase;
import org.example.constant.CommonStatusEnum;
import org.example.constant.PhoneLoginEnum;
import org.example.dao.mapper.TUserPhoneMapper;
import org.example.dao.pojo.TUserPhone;
import org.example.dto.UserDTO;
import org.example.dto.UserLoginDTO;
import org.example.dto.TUserPhoneDTO;
import org.example.interfaces.IAccountTokenRPC;
import org.example.interfaces.IdBuilderRpc;
import org.example.interfaces.enums.IdBuilderEnum;
import org.example.key.UserProviderCacheKey;
import org.example.service.TUserPhoneService;
import org.example.util.ConvertBeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sz
 * @description 针对表【t_user_phone_00】的数据库操作Service实现
 * @createDate 2024-06-16 10:02:42
 */
@Service
public class TUserPhoneServiceImpl extends ServiceImpl<TUserPhoneMapper, TUserPhone>
        implements TUserPhoneService {


    ConvertBeanBase convertBeanBase = new ConvertBeanBase<TUserPhone, TUserPhoneDTO>(TUserPhone.class, TUserPhoneDTO.class);


    @Resource
    private TUserPhoneMapper tUserPhoneMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserProviderCacheKey userProviderCacheKey;

    @DubboReference
    private IdBuilderRpc idBuilderRpc;

    @DubboReference
    private IAccountTokenRPC iAccountTokenRPC;

    @Resource
    private IUserServiceImpl userService;

    private final static String newUserNickNamePrefix = "手机号注册用户";

    @Override
    public UserLoginDTO login(String phone) {

        if (Strings.isNullOrEmpty(phone)) {
            return UserLoginDTO.loginError(PhoneLoginEnum.PHONE_EMPTY.getDesc());
        }


        //查询用户信息
        TUserPhoneDTO tUserPhoneDTO = this.queryByPhone(phone);

        if (tUserPhoneDTO != null&&null == tUserPhoneDTO.getUserId()) {

            //手机号不存在
            return UserLoginDTO.loginError(PhoneLoginEnum.PHONE_NO_EXIT.getDesc());
        } else {

            //创建新用户
            tUserPhoneDTO = registerNewPhoneUser(phone);
            redisTemplate.delete(userProviderCacheKey.buildUserPhoneKey(phone));
        }

        String loginToken = iAccountTokenRPC.createAndSaveLoginToken(tUserPhoneDTO.getUserId());
        return UserLoginDTO.loginSuccess(tUserPhoneDTO.getUserId(), phone, loginToken);
    }

    private TUserPhoneDTO registerNewPhoneUser(String phone) {
        TUserPhoneDTO tUserPhoneDTO;
        Integer userId = idBuilderRpc.increaseSeqId(IdBuilderEnum.USER_ID_STRATEGY.getCode());

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId.longValue());
        userDTO.setNickName(newUserNickNamePrefix + userId);

        userService.insertUserId(userDTO);

        tUserPhoneDTO = new TUserPhoneDTO();
        tUserPhoneDTO.setPhone(phone);
        tUserPhoneDTO.setUserId(userId.longValue());
        tUserPhoneDTO.setCreateTime(new Date());
        tUserPhoneDTO.setUpdateTime(new Date());
        tUserPhoneDTO.setStatus(CommonStatusEnum.PHONE_VAILD.getCode());

        this.insertTUserPhone(tUserPhoneDTO);
        return tUserPhoneDTO;
    }

    private String createLoginToken(Long userId) {
        String token = UUID.randomUUID().toString();
        String buildUserLoginToken = userProviderCacheKey.buildUserLoginToken(token);
        redisTemplate.opsForValue().set(buildUserLoginToken, userId, 24, TimeUnit.HOURS);
        return token;
    }

    @Override
    public List<TUserPhoneDTO> queryByUserId(Long userId) {

        String redisKey =
                userProviderCacheKey.buildUserPhoneList(userId);
        List<Object> userPhoneCacheList =
                redisTemplate.opsForList().range(redisKey, 0, -1);
        if (!CollectionUtils.isEmpty(userPhoneCacheList)) {
            //可能是缓存的空值
            if (((TUserPhoneDTO)userPhoneCacheList.get(0)).getUserId() == null) {
                return Collections.emptyList();
            }
            return userPhoneCacheList.stream().map(x->{return (TUserPhoneDTO)x;}).collect(Collectors.toList());
        }

        //底层会走用户手机号的主键索引
        LambdaQueryWrapper<TUserPhone> queryWrapper = new
                LambdaQueryWrapper<>();
        queryWrapper.eq(TUserPhone::getUserId, userId);
        queryWrapper.eq(TUserPhone::getStatus,
                CommonStatusEnum.PHONE_VAILD.getCode());

        List<TUserPhoneDTO> tUserPhoneDTOS = ConvertBeanUtils.convertList(this.baseMapper.selectList(queryWrapper), TUserPhoneDTO.class);

        int expireTime = 30;
        if (CollectionUtils.isEmpty(tUserPhoneDTOS)) {
            //防止缓存击穿
            userPhoneCacheList = Arrays.asList(new TUserPhoneDTO());
            expireTime = 5;
        }
        redisTemplate.opsForList().leftPushAll(redisKey,
                tUserPhoneDTOS.toArray());
        redisTemplate.expire(redisKey, expireTime,
                TimeUnit.MINUTES);
        return userPhoneCacheList.stream().map(x->{return (TUserPhoneDTO)x;}).collect(Collectors.toList());


    }

    @Override
    public TUserPhoneDTO queryByPhone(String phone) {

        if (Strings.isNullOrEmpty(phone)) {
            return null;
        }

        //从缓存中获取信息
        String buildUserPhoneKey = userProviderCacheKey.buildUserPhoneKey(phone);
        TUserPhoneDTO tUserPhoneDTO = (TUserPhoneDTO) redisTemplate.opsForValue().get(buildUserPhoneKey);

        if (tUserPhoneDTO == null) {

            QueryWrapper<TUserPhone> query = new QueryWrapper<>();
            query.eq("phone", phone);
            query.eq("status", CommonStatusEnum.PHONE_VAILD.getCode());
            query.orderByDesc("create_time");
            query.last("limit 1");
            TUserPhone tUserPhone = tUserPhoneMapper.selectOne(query);

            if (tUserPhone == null) {
                //不存在的手机号，缓存空值
                redisTemplate.opsForValue().set(buildUserPhoneKey, new TUserPhoneDTO(), 5, TimeUnit.MINUTES);
            } else {
                //缓存用户数据
                tUserPhoneDTO = (TUserPhoneDTO) convertBeanBase.convertToV(tUserPhone);
                redisTemplate.opsForValue().set(buildUserPhoneKey, tUserPhoneDTO, 30, TimeUnit.MINUTES);
            }


        }

        return tUserPhoneDTO;
    }

    @Override
    public boolean insertTUserPhone(TUserPhoneDTO tUserPhoneDTO) {
        if (tUserPhoneDTO == null) {
            return false;
        }
        TUserPhone tUserPhone = (TUserPhone) convertBeanBase.convertToK(tUserPhoneDTO);

        return this.baseMapper.insert(tUserPhone) > 0;

    }
}




