package com.example.postman.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhanjun date 2020-06-28
 */
@Configuration
@Slf4j
public class RedissonConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private String redisPort;

	@Value("${spring.redis.password}")
	private String redisPassword;

	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort).setPassword(redisPassword);
		/**
		 *  默认30s
		 *  监控锁的看门狗超时时间单位为毫秒。该参数只适用于分布式锁的加锁请求中未明确使用leaseTimeout参数的情况
		 */
		config.setLockWatchdogTimeout(2000L);
		RedissonClient client = null;
		try {
			client = Redisson.create(config);
		} catch (Exception e) {
			log.error("Redisson init failed {}", e.getMessage());
		}
		return client;
	}
}
