package com.zenas.mall.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: mall
 * @author: Zephyr
 * @create: 2021-08-02 19:17
 * JWT Token的生成的工具类
 * JWT token的格式：header.payload.signature
 * header中用于存放签名的生成算法
 * payload中用于存放用户名、token的生成时间和过期时间
 * signature为以header和payload生成的签名，一旦header和payload被篡改，验证将失败
 */
@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    // payload
    private static final String CLAIM_KEY_USERNAME = "sub";
    // payload
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据负载生成token
     *
     * @param claims 负载主体
     * @return 生成token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     *
     * @param token token
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }


    /**
     * 从token中获取当前用户名
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Claims claims = getClaimsFromToken(token);
            userName = claims.getSubject();
        } catch (Exception e) {
            LOGGER.info("JWT解析token失败:{}", token);
            userName = null;
        }
        return userName;
    }

    /**
     * 验证当前token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库查询出来的用户信息
     * @return 是否失效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = this.getUserNameFromToken(token);
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        if (username.equals(userDetails.getUsername()) && this.isTokenExpired(token)) {
            return true;
        }

        return false;
    }

    /**
     * 判断当前的token是否已经失效
     *
     * @param token token
     * @return 结果
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return !expiredDate.before(new Date());
    }

    /**
     * 获取当前token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成Token
     *
     * @param userDetails 从数据库里面查询出来的用户信息
     * @return token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }


    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        if (isTokenExpired(token)) {
            LOGGER.info("token已过期:{}", token);
            return null;
        } else {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }
}
