package com.example.springtestcontainers.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springtestcontainers.dao.CartDao;
import com.example.springtestcontainers.dto.ItemDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController {
	private final CartDao cartDao;


	@PostMapping("/{id}/cart")
	public String addItemToCart(@PathVariable Long id, @RequestBody ItemDto itemDto){
		cartDao.addItem(itemDto, id);
		log.info("customer {} - add item to cart  : {} ", id, itemDto);
		return "success";
	}

	@GetMapping("/{id}/cart")
	public ItemDto getCartById(@PathVariable Long id){
		log.info("customer {} - find item from cart  ", id);
		return cartDao.findById(id);
	}

}
