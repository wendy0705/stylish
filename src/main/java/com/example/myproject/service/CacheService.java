package com.example.myproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, Map<String, Object>> redisTemplateMap;

    public Map<String, Object> getProfileCacheData(String cacheKey) {
        try {
            return redisTemplateMap.opsForValue().get(cacheKey);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed. Falling back to database.", ex);
            return null;
        }
    }

    public void setProfileCacheData(String cacheKey, Map<String, Object> data, long timeout, TimeUnit unit) {
        try {
            redisTemplateMap.opsForValue().set(cacheKey, data, timeout, unit);
        } catch (RedisConnectionFailureException ex) {
            log.error("Failed to connect to Redis to set cache.", ex);
        }
    }


    public String getCampaignDataFromCache(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (RedisConnectionFailureException ex) {
            log.error("Redis connection failed. Falling back to database.", ex);
            return null;
        }
    }

    public void setCampaignDataToCache(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (RedisConnectionFailureException ex) {
            log.error("Failed to connect to Redis to set cache.", ex);
        }
    }

    public void clearCache(String key) {
        try {
            redisTemplate.delete(key);
        } catch (RedisConnectionFailureException ex) {
            log.error("Failed to connect to Redis to clear cache.", ex);
        }
    }
}


