package com.example.springarchunit.layer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.springarchunit.layer.repository.MemberRepository;
import com.example.springarchunit.layer.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;
	private final MemberService memberService;
}
