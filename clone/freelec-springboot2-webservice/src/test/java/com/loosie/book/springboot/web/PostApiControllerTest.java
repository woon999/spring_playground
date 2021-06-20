package com.loosie.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loosie.book.springboot.domain.posts.Posts;
import com.loosie.book.springboot.domain.posts.PostsRepository;
import com.loosie.book.springboot.web.dto.PostsSaveRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.loosie.book.springboot.web.dto.PostsUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class PostApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @After
    public void tearDown() throws Exception{
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
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
        mvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);

     }

     @Test
     @WithMockUser(roles = "USER")
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
         mvc.perform(put(url)
         .contentType(MediaType.APPLICATION_JSON_UTF8)
         .content(new ObjectMapper().writeValueAsString(requestDto)))
                 .andExpect(status().isOk());


         //then
         List<Posts> all = postsRepository.findAll();
         assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
         assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
      }

    @Test
    @WithMockUser(roles = "USER")
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
        mvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk());


        //then
        List<Posts> all = postsRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }


}
