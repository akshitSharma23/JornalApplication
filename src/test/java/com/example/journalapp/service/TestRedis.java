package com.example.journalapp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Test
    void testSendMail() {
        redisTemplate.opsForValue().set("HomeEmail","weeee@email.com");
        Object salary = redisTemplate.opsForValue().get("HomeEmail");
        System.out.println(salary);
        int a = 1;
    }
}