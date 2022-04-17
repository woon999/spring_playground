package com.example.springcacheredis.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.springcacheredis.model.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
}
