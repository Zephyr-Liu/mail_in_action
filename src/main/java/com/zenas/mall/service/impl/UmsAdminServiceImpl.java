package com.zenas.mall.service.impl;

import com.zenas.mall.common.util.JwtTokenUtil;
import com.zenas.mall.dao.UmsAdminRoleRelationDao;
import com.zenas.mall.mbg.mapper.UmsAdminMapper;
import com.zenas.mall.mbg.model.UmsAdmin;
import com.zenas.mall.mbg.model.UmsAdminExample;
import com.zenas.mall.mbg.model.UmsPermission;
import com.zenas.mall.service.UmsAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-05 17:24
 * UmsAdminService实现类
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${jwt.tokenHead}")
    private String tokenHead;


    private UmsAdminMapper adminMapper;

    @Autowired(required = false)
    public void setAdminMapper(UmsAdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Autowired
    public void setAdminRoleRelationDao(UmsAdminRoleRelationDao adminRoleRelationDao) {
        this.adminRoleRelationDao = adminRoleRelationDao;
    }

    private UmsAdminRoleRelationDao adminRoleRelationDao;

    /**
     * 根据用户名获取后台管理员
     *
     * @param username 后台管理员
     * @return 后台管理员
     */
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) return adminList.get(0);
        return null;
    }

    /**
     * 注册功能
     *
     * @param umsAdminParam 客户端传来的对象
     * @return 新的用户
     */
    @Override
    public UmsAdmin register(UmsAdmin umsAdminParam) {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, umsAdmin);
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setStatus(1);
        //查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (umsAdminList.size() > 0) return null;
        //将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    /**
     * 登录事件
     *
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            //校验用户名，封装实体类
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //校验密码
            LOGGER.info(userDetails.getPassword() + ":password");
            LOGGER.info(userDetails.getUsername() + ":username");
            if (!passwordEncoder.matches(password, userDetails.getPassword()))
                throw new BadCredentialsException("密码不正确");
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    /**
     * 获取用户所有权限（包括角色权限和+-权限）
     *
     * @param adminId 用户名id
     * @return 查询出来的权限信息
     */
    @Override
    public List<UmsPermission> getPermissionsList(Long adminId) {
        return adminRoleRelationDao.getPermissionList(adminId);
    }


}
