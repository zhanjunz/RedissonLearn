package com.example.postman.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String redisHello(@RequestParam("spuId") String spuId, @RequestParam("skuId") String skuId, @RequestParam("userId") String userId) {
        StringBuilder sb = new StringBuilder();
        String redisKey = sb.append(userId).append(spuId).append(skuId).toString();
        System.out.println("redisKey " + redisKey);
        RLock lock = redissonClient.getLock(redisKey);
        boolean isLock = false;
        try {
            isLock = lock.tryLock(500, 15 * 1000, TimeUnit.MILLISECONDS);
            System.out.println("Thread Id {}, isLock {}" + Thread.currentThread().getName() + " " + isLock);
            log.debug("Thread Id {}, isLock {}", Thread.currentThread().getId(), isLock);
        } catch (InterruptedException e) {
            log.error("get lock failed, exception {}", e.getMessage());
        }
        return lock.toString();
    }
}
