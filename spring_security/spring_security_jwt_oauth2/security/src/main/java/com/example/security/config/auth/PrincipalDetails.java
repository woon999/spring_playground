package com.example.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.security.model.User;

import lombok.Getter;

// 시큐리티가 /login 주소 요청이 오면 로그인 진행시킴
// 로그인 진행이 완료되면 시큐리티 전용 session에 유저 정보를 넣어줘야됨 (Security ContextHolder)
// 유저 정보 타입은 Authentication 객체이어야 한다 -> Authentication안에는 User정보가 있어야 됨
// User 정보를 담는 객체 타입은 UserDetails 객체이어야 한다.

// Security Session 안에 => Authentication 안에 => UserDetails 타입 필요
// 따라서, 로그인된 유저 정보를 UserDetails로 반환해야 함. (객체 반환은 PrincipalDetailsService가 진행)
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

	private User user;
	private Map<String, Object> attributes;

	// 일반 로그인
	public PrincipalDetails(User user) {
		this.user = user;
	}

	// OAuth 로그인
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return null;
	}

	// 해당 유저 권한 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 만료 안됐으면 true
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 잠금 안됐으면 true
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 개인정보 갱신 만료 안됐으면 true
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 사용가능하면 true
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}
}
