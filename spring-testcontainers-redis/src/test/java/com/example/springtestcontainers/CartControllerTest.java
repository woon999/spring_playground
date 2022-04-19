package com.example.springtestcontainers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springtestcontainers.dao.CartDao;
import com.example.springtestcontainers.dto.ItemDto;

@IntegrationTest
class CartControllerTest extends AbstractContainerBaseTest {

	@Autowired
	private CartDao cartDao;

	@Test
	void addCartItem(){
		ItemDto item = ItemDto.builder()
			.name("item")
			.price(1000)
			.quantity(2)
			.build();

		// when
		cartDao.addItem(item, 1L);

		ItemDto findItem = cartDao.findById(1L);
		assertEquals(findItem.getName(), "item");
		assertEquals(findItem.getPrice(), 1000);
		assertEquals(findItem.getQuantity(), 2);
	}

}