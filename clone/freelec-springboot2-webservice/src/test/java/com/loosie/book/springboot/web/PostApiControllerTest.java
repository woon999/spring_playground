package com.loosie.book.springboot.web;

import com.loosie.book.springboot.domain.posts.Posts;
import com.loosie.book.springboot.domain.posts.PostsRepository;
import com.loosie.book.springboot.web.dto.PostsSaveRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.loosie.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception{
        //given
        String title = "title";
        String content = "content";
        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                                            .title(title)
                                            .content(content)
                                            .author("author")
                                            .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

     }

     @Test
     public void Posts_수정된다() throws Exception{
         //given
         Posts savedPosts = postsRepository.save(Posts.builder()
                                .title("title")
                                .content("content")
                                .author("author")
                                .build()
         );

         Long updateId = savedPosts.getId();
         String expectedTitle = "title2";
         String expectedContent = "content2";

         PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                 .title(expectedTitle)
                 .content(expectedContent)
                 .build();
         String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

         HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

         //when
         ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

         //then
         assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
         System.out.println("####"+ responseEntity.getBody());
         assertThat(responseEntity.getBody()).isGreaterThan(0L);

         List<Posts> all = postsRepository.findAll();
         assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
         assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
      }

    @Test
    public void Posts_삭제된다() throws Exception{
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build()
        );

        Long deleteId = savedPosts.getId();
        String url = "http://localhost:" + port + "/api/v1/posts/" + deleteId;

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");

        //when
        restTemplate.delete(url, params);

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }


}
