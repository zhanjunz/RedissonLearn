package com.example.postman.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanjun date 2020-06-24
 */
@Slf4j
public class RedisUtil {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private String redisPort;

	@Value("${spring.redis.password}")
	private String redisPassword;
	private RedissonClient redissonClient;
	private RLock lock;

	@PostConstruct
	public void init() {
		String redisAddr = "redis://" + redisHost + ":" + redisPort;
		Config config = new Config();
		config.useSingleServer().setAddress(redisAddr).setPassword(redisPassword);
		try {
			redissonClient = Redisson.create(config);
		} catch (Exception e) {
			log.error("redis init failed {}", e.getMessage());
		}
	}

//	public boolean lock(long waitTime, long leaseTime) {
//		boolean isLock = false;
//		try {
//			lock = redissonClient.getLock("point.order");
//			// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
//			isLock = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			log.error("redis try lock failed {}", e.getMessage());
//			// Restore interrupted state..
//			Thread.currentThread().interrupt();
//			return false;
//		}
//		return isLock;
//	}

	public static void unLock(RLock lock) {
		if (lock != null) {
			try {
				if (lock.isLocked()) {
					lock.unlock();
					System.out.println("解锁成功");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("解锁失败");
			}
		} else {
			System.out.println("lock is null");
			// TODO throw exception
		}
	}

	public static boolean lock(RLock lock, long waitTime, long leaseTime) {
		boolean isLock = false;
		try {
			isLock = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
			int a=1/0;
			if(isLock) {
				System.out.println("加锁成功");
			}else {
				System.out.println("加锁失败");
			}

		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("加锁错误");
			unLock(lock);
			isLock = false;
			return isLock;
		}
		return isLock;
	}
}
