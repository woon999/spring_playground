package com.study.Jpawebapp.main;

import com.study.Jpawebapp.account.CurrentUser;
import com.study.Jpawebapp.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model){
        if (account != null){
            log.info("controller :" + account);
            model.addAttribute(account);
        }

        return "index";
    }

}
