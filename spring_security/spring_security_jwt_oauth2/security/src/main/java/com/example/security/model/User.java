package com.example.security.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * oauth2 google user
 * username = google_{sub}
 * password = "암호화(겟인데어)" > 의미없음
 * email = {email}
 * role = "ROLE_USER"
 * provider = "google"
 * providerId = {sub}
 */
@Entity
@Data
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String password;
	private String email;
	private String role;

	private String provider; // oauth (ex. google)
	private String providerId; // oauthId ( google.sub = 101897731656893138339)

	@CreationTimestamp
	private Timestamp createDate;

	@Builder
	public User(String username, String password, String email, String role, String provider,
		String providerId, Timestamp createDate) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
		this.createDate = createDate;
	}
}

