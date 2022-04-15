package com.example.springrediscache.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("Member")
public class Member implements Serializable {

	public enum Gender {
		MALE, FEMALE
	}

	@Id
	private Long id;
	private String name;
	private Gender gender;
	private int grade;
}
