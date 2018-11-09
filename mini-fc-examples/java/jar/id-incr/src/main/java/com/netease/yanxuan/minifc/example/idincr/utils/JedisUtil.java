/**
 * @(#)JedisUtil.java, 2018年09月11日.
 * Copyright 2018 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.netease.yanxuan.minifc.example.idincr.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author hzlvzimin
 */
public class JedisUtil {

    private static final JedisUtil INSTANCE = new JedisUtil();

    private static final String KEY_PREFIX = "id-incr:";

    private JedisPool jedisPool;

    private JedisUtil() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);
        jedisPool = new JedisPool(jedisPoolConfig, "10.216.41.160", 6379);
    }

    public static JedisUtil getInstance() {
        return INSTANCE;
    }

    public Long incrAndGet(String key) {
        Jedis jedis = jedisPool.getResource();
        Long value = jedis.incr(KEY_PREFIX + key);
        jedis.close();
        return value;
    }

}
