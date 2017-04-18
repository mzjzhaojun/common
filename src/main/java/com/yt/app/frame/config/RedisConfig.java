package com.yt.app.frame.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "redis", locations = "classpath:config/redis.properties")
public class RedisConfig {

	private String host;

	private int port;
	
	private String password;

	private int timeout;

	private int expire;
}