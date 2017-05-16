package com.yt.app.frame.d;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yt.app.frame.c.RedisConfig;

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

@Configuration
public class Ai extends CachingConfigurerSupport {

	@Autowired
	RedisConfig c;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory localJedisConnectionFactory = new JedisConnectionFactory();
		localJedisConnectionFactory.setHostName(this.c.getHost());
		localJedisConnectionFactory.setPort(this.c.getPort());
		localJedisConnectionFactory.setPassword(this.c.getPassword());
		localJedisConnectionFactory.setTimeout(this.c.getTimeout());
		localJedisConnectionFactory.setPoolConfig(m());
		localJedisConnectionFactory.setUsePool(true);
		return localJedisConnectionFactory;
	}

	public JedisPoolConfig m() {
		JedisPoolConfig localJedisPoolConfig = new JedisPoolConfig();
		localJedisPoolConfig.setMaxTotal(30);
		localJedisPoolConfig.setMaxIdle(6);
		localJedisPoolConfig.setMaxWaitMillis(1000L);
		localJedisPoolConfig.setMinEvictableIdleTimeMillis(30000L);
		localJedisPoolConfig.setNumTestsPerEvictionRun(3);
		localJedisPoolConfig.setTimeBetweenEvictionRunsMillis(60000L);
		return localJedisPoolConfig;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory paramRedisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		a(template);
		template.setConnectionFactory(paramRedisConnectionFactory);
		template.afterPropertiesSet();
		return template;
	}

	private void a(RedisTemplate<String, Object> paramRedisTemplate) {
		Jackson2JsonRedisSerializer<Object> localJackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		ObjectMapper localObjectMapper = new ObjectMapper();
		localObjectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		localObjectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		localJackson2JsonRedisSerializer.setObjectMapper(localObjectMapper);
		paramRedisTemplate.setValueSerializer(localJackson2JsonRedisSerializer);
	}
}