package com.example.jwt.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MyFilter1 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		System.out.println("필터 1");
		chain.doFilter(request, response); // 다시 필터 체인에 태워줘야 함. 안태워주면 필터 종료됨

	}
}
