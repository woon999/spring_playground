package com.example.springtestcontainers.dao;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.springtestcontainers.dto.ItemDto;
import com.example.springtestcontainers.util.KeyGen;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartDao {

	private final RedisTemplate<String, Object> redisTemplate;

	public void addItem(ItemDto itemDto, Long customerId){
		String key = KeyGen.cartKeyGenerate(customerId);

		redisTemplate.opsForValue().set(key, itemDto);
		redisTemplate.expire(key, 1, TimeUnit.MINUTES);
	}

	public ItemDto findById(Long customerId){
		String key = KeyGen.cartKeyGenerate(customerId);

		return (ItemDto) redisTemplate.opsForValue().get(key);
	}
}
