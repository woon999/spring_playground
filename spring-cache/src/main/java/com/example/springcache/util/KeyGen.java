package com.example.springcache.util;

import com.example.springcache.model.Member;

public class KeyGen {
	public static Object generate(Member member) {
		return member.getId() + ":" + member.getName();
	}
}
