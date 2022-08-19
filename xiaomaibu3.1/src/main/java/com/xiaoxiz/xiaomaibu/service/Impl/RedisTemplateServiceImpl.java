package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.service.RedisTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTemplateServiceImpl implements RedisTemplateService {

    //获取bean
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void setRedis(String key,String value) {
        redisTemplate.opsForValue().set(key, value,20, TimeUnit.DAYS);

    }

    @Override
    public String getRedis(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void hashPut(String key, Map<String, List> map) {
        redisTemplate.opsForHash().putAll(key,map);
    }

    @Override
    public void hashPutWebSocketSession(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key,map);
    }

    @Override
    public Map<Object, Object> hashGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    @Override
    public void hashPutValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key,hashKey,value);
    }

    @Override
    public Object hashGetValue(String key, String hashKey) {
        return  redisTemplate.opsForHash().get(key,hashKey);
    }

    @Override
    public void hashDelete(String key, String hashKey) {
        redisTemplate.opsForHash().delete(key,hashKey);
    }


}
