package com.example.springrediscache.dao;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.springrediscache.dto.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartDao {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public void addItem(ItemDto item, Long memberId){
		String key = "cart:"+memberId;

		try{
			redisTemplate.opsForValue().set(key, item);
			redisTemplate.expire(key, 60, TimeUnit.SECONDS);
		} catch (Exception e){
			throw e;
		}
	}

	public ItemDto findOneByMemberId(Long memberId){
		String key = "cart:"+memberId;
		Object item = redisTemplate.opsForValue().get(key);
		return objectMapper.convertValue(item, ItemDto.class);
	}
}
