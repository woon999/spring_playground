package com.example.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// /login 요청이 올때 해당 서비스 로직 동작
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService의 loadUserByUsername");
		User user = userRepository.findByUsername(username);
		System.out.println("user :" + user);
		return new PrincipalDetails(user);
	}
}
