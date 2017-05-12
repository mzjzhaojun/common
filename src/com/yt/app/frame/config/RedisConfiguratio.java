package com.yt.app.frame.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;


//
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfiguratio extends CachingConfigurerSupport {

	@Autowired
	RedisConfig redisconfig;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisconfig.getHost());
		factory.setPort(redisconfig.getPort());
		factory.setPassword(redisconfig.getPassword());
		factory.setTimeout(redisconfig.getTimeout());
		factory.setPoolConfig(jedisPoolConfig());
		factory.setUsePool(true);
		return factory;
	}

	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(30);
		jedisPoolConfig.setMaxIdle(6);
		jedisPoolConfig.setMaxWaitMillis(1000);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(30000);
		jedisPoolConfig.setNumTestsPerEvictionRun(3);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000);
		return jedisPoolConfig;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		setSerializer(template);
		template.setConnectionFactory(factory);
		template.afterPropertiesSet();
		return template;
	}

	private void setSerializer(RedisTemplate<String, Object> template) {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
	}
}