package com.example.postman.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.postman.utils.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author zhanjun date 2020-06-24
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
	public boolean redisHello(@RequestParam("spuId") String spuId, @RequestParam("skuId") String skuId,
			@RequestParam("userId") String userId) {
		StringBuilder sb = new StringBuilder();
		String redisKey = sb.append(userId).append(spuId).append(skuId).toString();
		System.out.println("redisKey " + redisKey);
		RLock lock = redissonClient.getLock(redisKey);
		boolean isLock = false;
		// 加锁
		// 过期时间8s
		isLock = RedisUtil.lock(lock, 200, 8 * 1000);

		if (isLock) {
			// 加锁成功
			try {
				// TODO business
				System.out.println("业务代码");
				Thread.sleep(2000);
//				int a =1/0;

				return true;
//				RedisUtil.unLock(lock);
			} catch (Exception e) {
				// TODO: handle exception
				RedisUtil.unLock(lock);
				System.out.println("业务报错");
				return false;
			}
		} else {
			System.out.println("提交过快，稍后再试");
			return false;
		}

	}
}
