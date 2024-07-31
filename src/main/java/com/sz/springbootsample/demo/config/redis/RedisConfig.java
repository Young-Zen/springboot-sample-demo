package com.sz.springbootsample.demo.config.redis;

import java.time.Duration;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Yanghj
 * @date 4/16/2020
 */
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Resource private RedisConnectionFactory redisConnectionFactory;

    @Value("${spring.redis.cachePrefix}")
    private String cachePrefix;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = jackson2JsonRedisSerializer();
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Override
    public CacheManager cacheManager() {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration redisCacheConfiguration =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(3600L))
                        .disableCachingNullValues()
                        .computePrefixWith(cacheName -> cachePrefix + cacheName)
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        stringRedisSerializer))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        jackson2JsonRedisSerializer()));
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                redisCacheConfiguration) {
            @Override
            protected RedisCache createRedisCache(
                    String name, RedisCacheConfiguration cacheConfig) {
                // 解析 name 获取过期时间
                String[] parts = name.split("#");
                String cacheName = parts[0];
                if (parts.length > 1) {
                    long expiration = Long.parseLong(parts[1]);
                    cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(expiration));
                }
                return super.createRedisCache(cacheName, cacheConfig);
            }
        };
    }

    private Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(new JavaTimeModule());
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }
}
