package com.example.springrediscache.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "name")
public class ItemDto implements Serializable {

	private String name;
}
