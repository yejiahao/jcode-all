package com.yejh.jcode.sboot.restful.controller;

import cn.hutool.core.lang.Dict;
import com.yejh.jcode.sboot.restful.config.RedisKeyExpirationListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2022-12-09
 * @since 1.0.0
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @PostMapping("/set")
    public boolean set(@RequestBody Dict kv) {
        String hKey = "HASH_KEY";
        String shadowHKey = RedisKeyExpirationListener.SHADOW_KEY_PREFIX + hKey;
        Long size = redisTemplate.opsForHash().size(hKey);

        redisTemplate.opsForHash().putAll(hKey, kv);
        redisTemplate.opsForHash().putAll(shadowHKey, kv);

        if (size == 0L) {
            redisTemplate.expire(hKey, RedisKeyExpirationListener.EXPIRE_DURATION);
        }

        return true;
    }
}
