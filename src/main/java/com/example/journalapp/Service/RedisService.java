package com.example.journalapp.Service;

import com.example.journalapp.Entity.WeatherEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public <T> T get(String key, Class<T> weatherEntity){
        try{
            Object obj=redisTemplate.opsForValue().get(key);
            if(obj==null)return null;
            ObjectMapper objectMapper=new ObjectMapper();
            return objectMapper.readValue(obj.toString(),weatherEntity);
        }catch (Exception e){
            log.error("Error in get value from redis",e);
            return null;
        }
    }
    public void set(String key,Object value, Long ttl){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            String str=objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key,str,ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("Error in get value from redis",e);
        }
    }

}
