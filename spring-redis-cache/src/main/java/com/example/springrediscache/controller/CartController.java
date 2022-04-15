package com.example.springrediscache.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springrediscache.dao.CartDao;
import com.example.springrediscache.dto.ItemDto;
import com.example.springrediscache.model.Product;
import com.example.springrediscache.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartDao cartDao;

	@PostMapping("/{id}")
	public void save(@RequestBody ItemDto item, @PathVariable Long id) {
		cartDao.addItem(item, id);

		log.info("add item to cache : " + item +"," + id);
	}

	@GetMapping("/{id}")
	public ItemDto findOneByMemberId(@PathVariable Long id) {
		ItemDto one = cartDao.findOneByMemberId(id);
		log.info("find item : " + id);
		return one;
	}
}
