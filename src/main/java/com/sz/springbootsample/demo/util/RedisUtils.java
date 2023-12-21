package com.sz.springbootsample.demo.util;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

/**
 * @author Yanghj
 * @date 4/16/2020
 */
public class RedisUtils {

    private RedisTemplate<String, Object> redisTemplate;

    private static class RedisUtilsHolder {
        private static final RedisUtils INSTANCE = new RedisUtils();
    }

    private RedisUtils() {
        redisTemplate =
                (RedisTemplate<String, Object>) ApplicationContextUtils.getBean("redisTemplate");
    }

    public static final RedisUtils getInstance() {
        return RedisUtilsHolder.INSTANCE;
    }

    /** 解锁脚本，原子操作 */
    private static final String UNLOCK_SCRIPT =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
                    + "then\n"
                    + "    return redis.call(\"del\",KEYS[1])\n"
                    + "else\n"
                    + "    return 0\n"
                    + "end";

    /**
     * Atomically increments by one the current value
     *
     * @param key
     * @return
     */
    public long generateID(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.incrementAndGet();
    }

    /**
     * 该加锁方法仅针对单实例 Redis 可实现分布式加锁 对于 Redis 集群则无法使用 集群用 redisson
     *
     * <p>支持重复，线程安全
     *
     * @param key 加锁键
     * @param clientId 加锁客户端唯一标识(采用UUID)
     * @param seconds 锁过期时间
     * @return
     */
    public Boolean tryLock(String key, String clientId, long seconds) {
        return redisTemplate.opsForValue().setIfAbsent(key, clientId, seconds, TimeUnit.SECONDS);
    }

    /**
     * 释放分布式锁
     *
     * @param key
     * @param clientId
     * @return
     */
    public Boolean unLock(String key, String clientId) {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(UNLOCK_SCRIPT);
        redisScript.setResultType(Boolean.class);
        return redisTemplate.execute(redisScript, Collections.singletonList(key), clientId);
    }
}
