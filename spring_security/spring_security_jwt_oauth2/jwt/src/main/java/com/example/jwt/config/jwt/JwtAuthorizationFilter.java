package com.example.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// 시큐리티가 filter를 가지고 있는데 그 필터 중에 BasicAuthenticationFilter라는 필터가 있다.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있다.
// 만약 권한, 인증이 필요한 주소가 아니라면 이 필터를 타지 않는다.
// -> 시큐리티 필터에 걸쳐주면 권한,인증 절차가 없더라도 무조건 거치게 되어있음

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	private final JwtProperties jwtProperties;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
		UserRepository userRepository, JwtProperties jwtProperties) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.jwtProperties = jwtProperties;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

		String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
		System.out.println("jwtHeader : " + jwtHeader);

		// jwt 토큰이 들어있지 않은 경우
		if(jwtHeader==null|| !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)){
			chain.doFilter(request,response);
			return;
		}

		// jwt 토큰을 검증해서 사용자 권한 인증
		String jwtToken = jwtHeader.replace(JwtProperties.TOKEN_PREFIX,"");
		String username = JWT
			.require(Algorithm.HMAC512(jwtProperties.getSecret()))
			.build()
			.verify(jwtToken)
			.getClaim("username").asString();

		// 서명이 정상적으로 됨
		if(username != null){
			User user = userRepository.findByUsername(username);
			PrincipalDetails principalDetails = new PrincipalDetails(user);

			// jwt토큰 유효 검증(username이 null이 아니기 때문에) 인증 객체를 만들어도 됨
			Authentication authentication =  new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			System.out.println("authentication : " + authentication );
			// 강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);

			chain.doFilter(request, response);

		}

	}
}
