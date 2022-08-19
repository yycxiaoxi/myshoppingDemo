package com.xiaoxiz.xiaomaibu.service;


import java.util.List;
import java.util.Map;

public interface RedisTemplateService {
    public void setRedis(String key,String value);
    public String getRedis(String key);
    public void hashPut(String key, Map<String, List> map);
    public void hashPutWebSocketSession(String key, Map<String, String> map);
    public Map<Object, Object> hashGet(String key);
    //public Map<String,WebSocketSession> hashGetWebSocketSession(String key);
    public void hashPutValue(String key,String hashKey,Object value);
    public Object hashGetValue(String key,String hashKey);
    public void hashDelete(String key,String hashKey);
}
