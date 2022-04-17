package com.example.springcacheredis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcacheredis.dao.CartDao;
import com.example.springcacheredis.dto.ItemDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

	private final CartDao cartDao;


	@PostMapping("/{id}")
	public String save(@PathVariable(name = "id") Long memberId, @RequestBody ItemDto itemDto){
		cartDao.addItem(itemDto, memberId);
		log.info("save cart to cache :" + memberId +" - [" + itemDto + "]");
		return "success caching";
	}

	@GetMapping("/{id}")
	public Object getByMemberId(@PathVariable(name = "id") Long memberId){
		log.info("find cart by member id :" + memberId);
		return cartDao.findByMemberId(memberId);
	}

}
