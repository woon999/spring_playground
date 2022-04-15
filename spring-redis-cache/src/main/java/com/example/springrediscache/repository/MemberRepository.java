package com.example.springrediscache.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springrediscache.model.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
}
