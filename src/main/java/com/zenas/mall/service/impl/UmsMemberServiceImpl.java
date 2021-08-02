package com.zenas.mall.service.impl;


import com.zenas.mall.service.RedisService;
import com.zenas.mall.service.UmsMemberService;
import com.zenas.mall.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-02 16:48
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    private RedisService redisService;

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;

    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            builder.append(random.nextInt(10));
        }
        //验证码绑定手机号并且存储到redis里面
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE+telephone,builder.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE+telephone,AUTH_CODE_EXPIRE_SECONDS);
        return CommonResult.success(builder.toString(),"获取验证码成功");
    }

    /**
     *    对输入的验证码进行校验
     * @param telephone  手机号码
     * @param authCode    验证码
     * @return 返回的结果
     */
    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)) {
            return CommonResult.failed("请输入验证码");
        }
        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE+telephone);
        boolean result = authCode.equals(realAuthCode);
        if (result) {
            return CommonResult.success(null, "验证码校验成功");
        }else {
            return CommonResult.failed("验证码不正确");
        }
    }
}
