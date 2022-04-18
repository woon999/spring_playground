package com.example.springcm4sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcm4sb.model.Post;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Long>{
}
