package com.study.Jpawebapp.settings;

import com.study.Jpawebapp.account.AccountService;
import com.study.Jpawebapp.account.CurrentUser;
import com.study.Jpawebapp.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class SettingsController {

    static final String SETTINGS_PROFILE_VIEW_NAME = "settings/profile";
    static final String SETTINGS_PROFILE_URL = "/settings/profile";

    private final AccountService accountService;

    @GetMapping(SETTINGS_PROFILE_URL)
    public String profileUpdateForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute(new Profile(account));

        return SETTINGS_PROFILE_VIEW_NAME;
    }

    // Errors 혹은 BingResult는 Form을 받는(@ModelAttribute Profile) 오른쪽에 두어야함
    // @ModelAttribute 생략가능
    @PostMapping(SETTINGS_PROFILE_URL)
    public String updateProfile(@CurrentUser Account account, @Valid Profile profile, Errors errors,
                                Model model, RedirectAttributes attributes){
        if(errors.hasErrors()){
            model.addAttribute(account);
            return SETTINGS_PROFILE_VIEW_NAME;
        }

        accountService.updateProfile(account, profile);

        // MVC에서 제공. 잠깐 한 번 쓰고 버리는 데이터(flash). Model에 잠깐 들어가고 없어진다
        attributes.addFlashAttribute("message", "프로필을 수정했습니다.");

        return "redirect:" + SETTINGS_PROFILE_URL;
    }
}
