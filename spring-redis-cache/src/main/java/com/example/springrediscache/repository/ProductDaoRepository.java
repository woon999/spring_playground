package com.example.springrediscache.repository;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.springrediscache.domain.Product;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductDaoRepository {
	public static final String HASH_KEY = "Product";

	private final RedisTemplate redisTemplate;

	public Product save(Product product){
		redisTemplate.opsForHash().put(HASH_KEY, product.getId(), product);
		return product;
	}

	public List<Product> findAll(){
		return redisTemplate.opsForHash().values(HASH_KEY);
	}

	public Product findProductById(Long id){
		System.out.println("called findProductById() from DB");
		return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
	}

	public String delete(Long id){
		redisTemplate.opsForHash().delete(HASH_KEY, id);
		return id +"is removed";
	}

}
