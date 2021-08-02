package com.zenas.mall.service;

/**
 * @program: mall
 * @description: Redis操作Service 对象和数组都以json形式进行存储
 * @author: Zephyr
 * @create: 2021-08-02 15:02
 */
public interface RedisService {
    /**
     *  存储数据
     * @param key key 钥匙
     * @param value 值
     */
    void set(String key,String value);

    /**
     *
     * @param key 传送过来的key
     * @return 返回数据
     */
    String get(String key);

    /**
     *  设置超期时间
     */
    boolean expire(String key, long expire);

    /**
     *  删除数据
     * @param key 参数key
     */
    void remove(String key);

    /**
     *  自增操作
     * @param delta 自增步长
     */
    Long increment(String key,long delta);
}
