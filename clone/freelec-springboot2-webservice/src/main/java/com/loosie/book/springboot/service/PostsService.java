package com.loosie.book.springboot.service;

import com.loosie.book.springboot.domain.posts.Posts;
import com.loosie.book.springboot.domain.posts.PostsRepository;
import com.loosie.book.springboot.web.dto.PostsListResponseDto;
import com.loosie.book.springboot.web.dto.PostsResponseDto;
import com.loosie.book.springboot.web.dto.PostsSaveRequestDto;
import com.loosie.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    // 게시글 저장
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    // 게시글 수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        // 영속성 컨텍스트가 트랜잭션 끝나는 시점에 자동으로 Update 쿼리 전송 (dirty checking)
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    // 게시글 조회 (id)
    @Transactional
    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }

    // 모든 게시글 조회 (내림차순)
    @Transactional
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    // 게시글 삭제
    public void delete(Long id){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        postsRepository.delete(posts);
    }

}
