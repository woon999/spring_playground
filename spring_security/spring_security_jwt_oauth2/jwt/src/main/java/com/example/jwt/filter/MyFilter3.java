package com.example.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {

		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;

		// if token = 'cos' -> (id, pw) 정상적으로 로그인 완료되면 jwt 토큰 생성 후 응답하면 된다.
		// 요청할 때 마다 header에 Authorization에 value 값으로 토큰을 가져오면 해당 토큰이 유효한 토큰인지 검증하면 된다(HS256 or RSA)
		if (req.getMethod().equals("POST")) {
			System.out.println("POST 요청됨");
			System.out.println("security before 필터 3");
			String headerAuth = req.getHeader("Authorization");

			if (headerAuth.equals("cos")) {
				System.out.println(headerAuth);
				chain.doFilter(req, res); // 다시 필터 체인에 태워줘야 함. 안태워주면 필터 종료됨
			} else {
				PrintWriter out = res.getWriter();
				out.println("token 인증 실패");
			}
		}

	}
}
