package com.sz.springbootsample.demo.util;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Yanghj
 * @date 2024/8/1 9:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedisUtilsTest {

    @Test
    public void testDistributedLock() {
        String redisKey = "testDistributedLock";
        String clientId = UUID.randomUUID().toString();
        try {
            if (RedisUtils.getInstance().tryLock(redisKey, clientId, 300)) {
                assertFalse(RedisUtils.getInstance().tryLock(redisKey, clientId, 300));
            }
        } finally {
            RedisUtils.getInstance().unLock(redisKey, clientId);
            assertTrue(RedisUtils.getInstance().tryLock(redisKey, clientId, 1));
        }
    }
}
