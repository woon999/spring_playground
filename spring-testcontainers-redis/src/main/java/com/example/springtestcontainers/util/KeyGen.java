package com.example.springtestcontainers.util;

public class KeyGen {

	public static String cartKeyGenerate(Long customerId) {
		return "cart:" + customerId;
	}

}
