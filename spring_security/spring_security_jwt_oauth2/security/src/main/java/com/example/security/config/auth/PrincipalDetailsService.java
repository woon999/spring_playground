package com.example.security.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.security.model.User;
import com.example.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// 시큐리티 설정에서 loginProcssUrl("/login")
// login 요청이 오면 자동으로 UserDetailsService타입으로 등록되어 있는 빈을 실행
// 해당 빈에 들어있는 loadUserByUsername를 동작시킴 > UserDetails 객체 반환
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	// 시큐리티 session안에 ([UserDetails로 포장된] Authentication 객체)가 들어와야 됨.
	// 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user != null){
			return new PrincipalDetails(user);
		}
		return null;
	}
}
