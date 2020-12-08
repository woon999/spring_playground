package com.study.Jpawebapp.main;

import com.study.Jpawebapp.account.AccountRepository;
import com.study.Jpawebapp.account.AccountService;
import com.study.Jpawebapp.account.SignUpForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void init(){
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("jongwon");
        signUpForm.setEmail("jong9712@naver.com");
        signUpForm.setPassword("12341234");
        accountService.processNewAccount(signUpForm);
    }

    // 회원정보가 중복으로 DB에 들어가니 삭제해줘야함
    @AfterEach
    void finish(){
        accountRepository.deleteAll();
    }

    @DisplayName("이메일로 로그인 성공")
    @Test
    public void login_with_email() throws Exception{
        // email이 아니라 nickname으로 인증하는 이유는
        // AccountUser에서 nickname과 password 반환해주기 때문에 그 값으로 인증
        mockMvc.perform(post("/login")
                        .param("username", "jong9712@naver.com")
                        .param("password", "12341234")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirect 발생
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jongwon"));
     }

    @DisplayName("닉네임으 로그인 성공")
    @Test
    public void login_with_nickname() throws Exception{
        // email이 아니라 nickname 이유는
        // AccountUser에서 nickname과 password 반환해주기 때문에 그 값으로 인증
        mockMvc.perform(post("/login")
                .param("username", "jongwon")
                .param("password", "12341234")
                .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirect 발생
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("jongwon"));
    }

    @DisplayName("로그인 실패")
    @Test
    public void login_fail() throws Exception{
        mockMvc.perform(post("/login")
                .param("username", "1111")
                .param("password", "00000000")
                .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirect 발생
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @WithMockUser // security.user에 있는 가짜정보 넣어줌
    @DisplayName("로그아웃")
    @Test
    public void logout() throws Exception{
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection()) // redirect 발생
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }
}