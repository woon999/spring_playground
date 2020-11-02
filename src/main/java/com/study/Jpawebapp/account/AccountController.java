package com.study.Jpawebapp.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/sign-up")
    public String signUpForm(Model model){
//        model.addAttribute("signUpForm", new SignUpForm());
//        아래와 동일 "signUpForm" -> 동일한 이름으로 알아서 찾아감
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }


}
