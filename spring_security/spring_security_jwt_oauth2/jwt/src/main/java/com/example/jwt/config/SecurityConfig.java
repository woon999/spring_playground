package com.example.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.example.jwt.filter.MyFilter1;
import com.example.jwt.filter.MyFilter3;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CorsFilter corsFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
		http.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(corsFilter) // @CrossOrigin(인증x), 인증이 필요할 때는 시큐리티 필터에 corsFilter 등록해줘야 함
			.formLogin().disable()
			.httpBasic().disable()
			.authorizeRequests()
			.antMatchers("/api/v1/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/manager/**")
			.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN') ")
			.antMatchers("/api/v1/admin/**")
			.access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll();
	}
}
