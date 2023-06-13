package com.yejh.jcode.sboot.restful.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    public static final String SHADOW_KEY_PREFIX = "SHADOW:";

    public static final Duration EXPIRE_DURATION = Duration.ofSeconds(30L);

    @Resource
    private RedisTemplate redisTemplate;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        String shadowKey = SHADOW_KEY_PREFIX + key;
        log.info("expired redis key: {}", key);

        // 模拟业务逻辑
        Map entries = redisTemplate.opsForHash().entries(shadowKey);
        entries.forEach((k, v) -> log.info("key: {}, value: {}", k, v));

        // 删除影子键值对
        Boolean ret = redisTemplate.delete(shadowKey);
        log.info("manual delete: {}", ret);
    }
}
