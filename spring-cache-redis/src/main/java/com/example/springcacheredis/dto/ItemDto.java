package com.example.springcacheredis.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ItemDto implements Serializable {
	private String name;
	private int price;
	private int quantity;
}
