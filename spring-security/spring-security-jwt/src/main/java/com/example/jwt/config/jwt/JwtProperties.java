package com.example.jwt.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
	public String expirationTime;
	public String secret;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";

	public int getExpirationTime() {
		return Integer.parseInt(expirationTime);
	}
}
