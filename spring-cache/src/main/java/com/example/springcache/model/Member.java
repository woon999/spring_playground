package com.example.springcache.model;

import java.io.Serializable;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Member {

	@Id
	private Long id;
	private String name;
	private Gender gender;
	private int grade;

	@Builder
	public Member(Long id, String name, Gender gender, int grade) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.grade = grade;
	}

	public void update(Member member) {
		this.name = member.getName();
		this.grade = member.getGrade();
	}
}
