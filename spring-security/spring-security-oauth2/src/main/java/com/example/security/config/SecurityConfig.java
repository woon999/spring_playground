package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.security.config.oauth.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

// oauth2 과정
// 1.코드받기(인증), 2.액세스토큰(권한),
// 3-1.사용자프로필 정보를 가지고 회원가입 진행
// 3-2. 기본 데이터 (이메일, 전화번호, 이름, 아이디)
// 쇼핑몰 -> (집주소) 정보 필요 / 백화점몰 -> (vip, 일반등급) 정보 필요

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public BCryptPasswordEncoder encodePassword(){
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
				.formLogin()
					.loginPage("/loginForm")
					.loginProcessingUrl("/login") //login주소가 호출되면 security에서 낚아채서 대신 로그인 진행
					.defaultSuccessUrl("/")
			.and()
				.oauth2Login()
					.userInfoEndpoint() // 구글 로그인이 완료되면 코드가 아닌 (엑세스 토큰 + 사용자 프로필 정보)를 받음
						.userService(customOAuth2UserService);

	}
}
