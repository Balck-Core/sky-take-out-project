package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

/**
 * @Description SpringDataRedisTest
 * @Author CoreryBlack
 * @Date 2024-10-09
 */

@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;
    
    
    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
        //string数据操作
        etOperations = redisTemplate.opsForSet();
    }
}
