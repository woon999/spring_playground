package com.example.springcm4sb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcm4sb.model.Post;
import com.example.springcm4sb.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

	private final PostRepository postRepository;


	@PostMapping("/{id}")
	public void save(@RequestBody Post post){
		postRepository.save(post);
	}

	@GetMapping("/{id}")
	public Post getOne(@PathVariable Long id){
		return postRepository.findById(id).orElseThrow(NoSuchFieldError::new);
	}


	@PutMapping("/{id}")
	public void update(@PathVariable Long id, @RequestBody Post post){
		Post findPost = postRepository.findById(id).orElseThrow(NoSuchFieldError::new);
		findPost.update(post);
		postRepository.deleteById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteOne(@PathVariable Long id){
		postRepository.deleteById(id);
	}



}
