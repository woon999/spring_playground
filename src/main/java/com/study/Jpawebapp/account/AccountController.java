package com.study.Jpawebapp.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;


    /**
     * SignUpForm데이터를 받을때 바인더를 설정
     * public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors)
     * @Valid SignUpForm이랑 매핑
     */
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(signUpFormValidator);
    }


    @GetMapping("/sign-up")
    public String signUpForm(Model model){
//        model.addAttribute("signUpForm", new SignUpForm());
//        아래와 동일 "signUpForm" -> 동일한 이름으로 알아서 찾아감
        model.addAttribute(new SignUpForm());
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors){
        if (errors.hasErrors()){
            return "account/sign-up";
        }


        // TODO : 회원가입 처리
        return "redirect:/";

    }

}
