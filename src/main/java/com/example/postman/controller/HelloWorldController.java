package com.example.postman.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanjun
 * date 2020-06-24
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class HelloWorldController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/redis")
    public String redisHello() {
        RLock lock = redissonClient.getLock("test");
        boolean isLock = false;
        try {
            isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("get lock failed, exception {}", e.getMessage());
        }
        return lock.toString();
    }
}
