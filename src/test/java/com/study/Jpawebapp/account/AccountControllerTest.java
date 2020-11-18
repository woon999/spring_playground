package com.study.Jpawebapp.account;

import com.study.Jpawebapp.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test
    public void checkedEmailToken_with_wrong_input() throws Exception{
        mockMvc.perform(get("/check-email-token")
                .param("token" , "asdafasd")
                .param("email", "email@email.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("account/checked-email"));

     }


    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test
    public void checkedEmailToken() throws Exception{
        //given
        Account account = Account.builder()
                        .email("test@email.com")
                        .password("12345678")
                        .nickname("loosie")
                        .build();
        Account newAccount = accountRepository.save(account);
        newAccount.generateEmailCheckToken();

        //then
        mockMvc.perform(get("/check-email-token")
                .param("token" , newAccount.getEmailCheckToken())
                .param("email", newAccount.getEmail() ))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"));



    }


    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test
    public void signUpForm() throws Exception{
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));

     }

     @DisplayName("회원 가입 처리 - 입력값 오류")
     @Test
    void signUpSubmit_with_wrong_input() throws Exception{
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jongwon")
                .param("email", "jong9712@naver.com")
                .param("password", "12345678")
                .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(view().name("account/sign-up"))
                //--올바른 데이터를 입력했을 경우 redirect//
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

         Account account = accountRepository.findByEmail("jong9712@naver.com");
         assertNotNull(account);
         assertNotEquals(account.getPassword(), "12345678");
         assertNotNull(account.getEmailCheckToken());

        assertTrue(accountRepository.existsByEmail("jong9712@naver.com"));
        then(javaMailSender).should().send(any(SimpleMailMessage.class));

     }





}