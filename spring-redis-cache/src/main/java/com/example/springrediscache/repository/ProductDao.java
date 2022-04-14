package com.example.springrediscache.repository;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Product")
public class ProductDao implements Serializable {
	@Id
	private Long id;
	private String name;
	private int quantity;
	private long price;
}
