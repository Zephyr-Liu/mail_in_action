package com.zenas.mall.controller;

import com.zenas.mall.common.api.CommonResult;
import com.zenas.mall.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-02 16:36
 *  会员登录注册管理Controller
 */
@Controller
@RequestMapping("/sso")
@Api(tags = "UmsMemberController",description = "会员登录注册管理")
public class UmsMemberController {
    private UmsMemberService umsMemberService;

    @Autowired
    public void setUmsMemberService(UmsMemberService umsMemberService) {
        this.umsMemberService = umsMemberService;
    }

    /**
     *
     * @param telephone 传输过来的验证码
     * @return 返回的结果
     */
    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam String telephone){
        return umsMemberService.generateAuthCode(telephone);
    }

    @ApiOperation("判断验证码是否正确")
    @RequestMapping(value ="/verifyAuthCode",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult judgeAuthCode(@RequestParam  String telephone,@RequestParam String authCode){
        return umsMemberService.verifyAuthCode(telephone, authCode);
    }

}
