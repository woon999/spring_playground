package com.example.springcache.controller;

import java.util.Collection;

import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcache.model.Member;
import com.example.springcache.repository.MemberRepository;

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
		log.info("save to cache :" + member);
		return memberRepository.save(member);
	}

	@PostMapping("/keygen")
	@Cacheable(key = "T(com.example.springcache.util.KeyGen).generate(#member)", cacheNames = "Member")
	public Member saveKeyGen(@RequestBody Member member){
		log.info("save to cache :" + member);
		return memberRepository.save(member);
	}

	@GetMapping
	public Iterable<Member> getAll() {
		log.info("get all member");
		return memberRepository.findAll();
	}

	@GetMapping("/{id}")
	@Cacheable(key = "#id", cacheNames = "Member")
	public Member findOne(@PathVariable Long id) {
		Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 멤버는 존재하지 않습니다."));
		log.info("Member fetching from DB :" + id);
		return member;
	}

	@PutMapping("/{id}")
	// @CachePut(key = "#id", cacheNames = "Member")
	public Member update(@PathVariable Long id, @RequestBody Member member) {
		Member findMember = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 멤버는 존재하지 않습니다."));

		findMember.update(member);
		memberRepository.save(findMember);

		log.info("Member update  :" + id);
		return findMember;
	}

	@DeleteMapping("/{id}")
	@CacheEvict(key = "#id", cacheNames = "Member")
	public String deleteOne(@PathVariable Long id) {
		memberRepository.deleteById(id);
		log.info("Member delete from Cache :" + id);
		return "delete";
	}

}
