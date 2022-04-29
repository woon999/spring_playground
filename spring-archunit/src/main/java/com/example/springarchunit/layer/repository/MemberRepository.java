package com.example.springarchunit.layer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springarchunit.layer.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
