package com.zenas.mall.controller;

import com.zenas.mall.common.api.CommonResult;
import com.zenas.mall.dto.UmsAdminLoginParam;
import com.zenas.mall.mbg.model.UmsAdmin;
import com.zenas.mall.mbg.model.UmsPermission;
import com.zenas.mall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-04 22:21
 * <p>
 * 后台用户管理
 */
@Controller
@Api(tags = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {


    private UmsAdminService adminService;

    @Autowired
    public void setUmsAdminService(UmsAdminService umsAdminService) {
        adminService = umsAdminService;
    }

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * @param umsAdminParam 前端传来的数据
     * @param result        校验参数
     * @return 是否成功的结果
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册")
    @ResponseBody
    public CommonResult<UmsAdmin> register(@RequestBody UmsAdmin umsAdminParam, BindingResult result) {
        UmsAdmin umsAdmin = adminService.register(umsAdminParam);
        if (StringUtils.isEmpty(umsAdmin)) CommonResult.failed();
        return CommonResult.success(umsAdmin);
    }


    /**
     * @param adminLoginParam 自定义的用户登录参数类  username,password
     * @param result          校验
     * @return 是否成功登录的结果
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录后以后返回token")
    public CommonResult login(@RequestBody UmsAdminLoginParam adminLoginParam, BindingResult result) {
        String token = adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword());
        if (StringUtils.isEmpty(token)) return CommonResult.validatedFailed("用户名或密码不正确");
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    /**
     * @param adminId 当前登录用户的id
     * @return 返回查询出来的所有权限的结果
     */
    @ApiOperation(value = "获取当前用户所有权限(包括+-权限)")
    @ResponseBody
    @RequestMapping(value = "/permission/{adminId}", method = RequestMethod.GET)
    public CommonResult<List<UmsPermission>> getPermissionsList(@PathVariable Long adminId) {
        List<UmsPermission> permissionsList = adminService.getPermissionsList(adminId);
        return CommonResult.success(permissionsList);
    }
}
