package com.example.springrediscache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

	// @Bean
	// public JedisConnectionFactory connectionFactory() {
	// 	RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
	// 	configuration.setHostName("localhost");
	// 	configuration.setPort(6379);
	// 	return new JedisConnectionFactory(configuration);
	// }
	//
	// @Bean
	// public RedisTemplate<String, Object> template(){
	// 	RedisTemplate<String, Object> template = new RedisTemplate<>();
	// 	template.setConnectionFactory(connectionFactory());
	// 	template.setKeySerializer(new StringRedisSerializer());
	// 	template.setHashKeySerializer(new StringRedisSerializer());
	// 	template.setHashKeySerializer(new JdkSerializationRedisSerializer());
	// 	template.setValueSerializer(new JdkSerializationRedisSerializer());
	// 	template.setEnableTransactionSupport(true);
	// 	template.afterPropertiesSet();
	// 	return template;
	// }

	// lettuce
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory("localhost", 6379);
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}

