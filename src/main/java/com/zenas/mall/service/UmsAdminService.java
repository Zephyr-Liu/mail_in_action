package com.zenas.mall.service;

import com.zenas.mall.mbg.model.UmsAdmin;
import com.zenas.mall.mbg.model.UmsPermission;

import java.util.List;

/**
 * 后台管理员Service
 */
public interface UmsAdminService {

    /**
     * 根据用户名获取后台管理员
     *
     * @param username 后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     *
     * @param umsAdminParam 客户端传来的对象
     */
    UmsAdmin register(UmsAdmin umsAdminParam);

    /**
     * 登录功能
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 获取用户所有权限(包括角色权限+-权限)
     *
     * @param adminId 用户名id
     */
    List<UmsPermission> getPermissionsList(Long adminId);


}
