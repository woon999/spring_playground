package com.loosie.book.springboot.service;

import com.loosie.book.springboot.domain.posts.Posts;
import com.loosie.book.springboot.domain.posts.PostsRepository;
import com.loosie.book.springboot.web.dto.PostsResponseDto;
import com.loosie.book.springboot.web.dto.PostsSaveRequestDto;
import com.loosie.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        // 영속성 컨텍스트가 트랜잭션 끝나는 시점에 자동으로 Update 쿼리 전송 (dirty checking)
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }
}
