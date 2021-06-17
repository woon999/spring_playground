package com.loosie.book.springboot.domain.posts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() throws Exception{
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                        .title(title)
                        .content(content)
                        .author("loosie@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

     }

     @Test
     public void BaseTimeEntity_등록() throws Exception{
         //given
         LocalDateTime now = LocalDateTime.of(2021,06,17,0,0,0);
         postsRepository.save(Posts.builder()
                 .title("title")
                 .content("content")
                 .author("author")
                 .build());

         //when
         List<Posts> postsList = postsRepository.findAll();

         //then
         Posts posts = postsList.get(0);

         System.out.println(">>>>>> createdDate="+posts.getCreatedDate()+", modifiedDate="+posts.getModifiedDate());

         assertThat(posts.getCreatedDate()).isAfter(now);
         assertThat(posts.getModifiedDate()).isAfter(now);
      }


}