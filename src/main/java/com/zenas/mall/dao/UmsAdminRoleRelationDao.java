package com.zenas.mall.dao;

import com.zenas.mall.mbg.model.UmsPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-05 17:37
 * 后台角色管理自定义Dao
 */
public interface UmsAdminRoleRelationDao {
    /**
     * 获取用户所有权限(包括+-权限)
     *
     * @param adminId 用户id
     * @return List<UmsPermission>
     * 自定义写sql语句获取权限
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
}
