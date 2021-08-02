package com.zenas.mall.service;

import com.zenas.mall.common.api.CommonResult;

/**
 *  会员管理Service
 */
public interface UmsMemberService {
    /**
     *    生成验证码
     * @param telephone 手机号
     */
    CommonResult generateAuthCode(String telephone);

    /**
     *  判断验证码和手机号是否正确
     * @param telephone  手机号码
     * @param authCode    验证码
     */
    CommonResult verifyAuthCode(String telephone,String authCode);
}
