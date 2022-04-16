package com.example.springcache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springcache.model.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
}
