package com.example.springarchunit.layer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springarchunit.layer.controller.MemberController;
import com.example.springarchunit.layer.repository.MemberRepository;

@Service
public class MemberService {


	@Autowired
	private MemberRepository memberRepository;

	/**
	 * rule check error!
	 */
	// @Autowired
	// private MemberController memberController;
}
