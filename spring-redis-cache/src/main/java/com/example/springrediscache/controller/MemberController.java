package com.example.springrediscache.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springrediscache.model.Member;
import com.example.springrediscache.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberRepository memberRepository;

	@PostMapping
	public Member save(@RequestBody Member member) {
		return memberRepository.save(member);
	}

	@GetMapping
	public Iterable<Member> getAll() {
		Iterable<Member> all = memberRepository.findAll();
		return all;
	}

	@GetMapping("/{id}")
	public Member findOne(@PathVariable Long id) {
		log.info("Employee fetching from Redis :" + id);
		Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 멤버는 존재하지 않습니다."));
		return member;
	}

}
