package com.study.Jpawebapp.main;

import com.study.Jpawebapp.account.CurrentAccount;
import com.study.Jpawebapp.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {


    /**
     * 인증된 사용자 정보 참조
     */
    @GetMapping("/")
    public String home(@CurrentAccount Account account, Model model){
        if (account != null){
            // account != null && !account.emailVerified -> 이메일 인증 메시지 노출
            model.addAttribute(account);
        }

        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
