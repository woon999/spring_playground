package com.study.Jpawebapp.settings;

import com.study.Jpawebapp.WithAccount;
import com.study.Jpawebapp.account.AccountRepository;
import com.study.Jpawebapp.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    // 테스트 실행 후에는 테스트로 만든 계정을 다시 지워야함
    @AfterEach
    void afterEach(){
        accountRepository.deleteAll();
    }

    @WithAccount("loosie")  // 인증된 유저정보 없으면 오류
    @DisplayName("프로필 수정 폼")
    @Test
    void updateProfileForm() throws Exception{
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }


    @WithAccount("loosie")
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception{
        String bio = "짧은 소개를 수정하는 경우";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                    .param("bio", bio)
                    .with(csrf()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                    .andExpect(flash().attributeExists("message"));

        Account loosie = accountRepository.findByNickname("loosie");
        assertEquals(bio, loosie.getBio());

     }

    @WithAccount("loosie")
    @DisplayName("프로필 수정하기 - 입력값 에러")
    @Test
    void updateProfile_error() throws Exception{
        String bio = "35자 이상 에러 길게 소개를 수정하는 경우길게 소개를 수정하는 경우길게 소개를 수정하는 경우길게 소개를 수정하는@Length(max = 50)@Length(max = 50)@Length(max = 50)@Length(max = 50)@Length(max = 50)@Length(max = 50) 경우길게 소개를 수정하는 경우";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account loosie = accountRepository.findByNickname("loosie");
        assertNull(loosie.getBio());

    }
       

}