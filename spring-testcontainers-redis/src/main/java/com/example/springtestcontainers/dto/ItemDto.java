package com.example.springtestcontainers.dto;

import java.io.Serializable;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemDto implements Serializable {
	private String name;
	private int price;
	private int quantity;

	@Builder
	public ItemDto(String name, int price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
}
