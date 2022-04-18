package com.example.springcm4sb.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.springframework.util.Assert;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(nullable = false)
	private String title;

	@Lob
	@Column(nullable = false)
	private String content;

	@Column(nullable = false, unique = true)
	private String link;

	@Column(nullable = false)
	private String writer;

	@Builder
	public Post(Long id, String title, String content, String link, String writer) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.link = link;
		this.writer = writer;
	}

	public void update(Post post) {
		this.title = post.getTitle();
		this.content = post.getContent();
		this.link = post.getLink();
		this.writer = post.getWriter();
	}
}
