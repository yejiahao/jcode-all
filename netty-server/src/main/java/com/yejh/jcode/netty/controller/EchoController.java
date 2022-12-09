package com.yejh.jcode.netty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:yejh.1248@qq.com">Ye Jiahao</a>
 * @create 2022-12-09
 * @since 1.0.0
 */
@RestController
@RequestMapping("/echo")
public class EchoController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
