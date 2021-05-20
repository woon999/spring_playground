package com.study.Jpawebapp.account;

import com.study.Jpawebapp.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

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

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);

        return "redirect:/";

    }

    /**
     * 이메일 인증되었는지 확인
     */
    @GetMapping("/check-email")
    public String checkEmail(@CurrentAccount Account account, Model model){
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    /**
     * 인증 이메일 다시 보내기
     * 10분에 1번만 재전송 가능
     */
    @GetMapping("/resend-confirm-email")
    public String resendConfirmEmail(@CurrentAccount Account account, Model model){
        if(!account.canSendConfirmEmail()){
            model.addAttribute("error", "인증 이메일은 1시간에 한번만 전송할 수 있습니다.");
            model.addAttribute("email", account.getEmail());
            return "account/check-email";
        }

        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    /**
     * 이메일 인증하기 (token, email정보 올바르게 입력)
     */
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model){
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if(account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        if(!account.isValidToken(token)){
            model.addAttribute("error", "wrong.token");
            return view;
        }

        //트랜잭션으로 감싸주기
        accountService.completeSignUp(account);

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;

    }


    /**
     * 프로필 정보 보여주기
     */
    @GetMapping("/profile/{nickname}")
    public String viewProfile(@PathVariable String nickname, Model model, @CurrentAccount Account account){

        //nickname을 가진 유저가 있는지 확인
        Account byNickname = accountRepository.findByNickname(nickname);
        if(nickname == null){
            throw new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다.");
        }

        /**
         *  model.addAttribute(byNickname); == model.addAttribute("account", byNickname);
         *  "" 선언 안해주면 byNickname객체 이름이 camelcase로 자동으로 삽입됨 -> signUpForm메소드에서 말한 것과 동일한 내용
         */
        model.addAttribute(byNickname);
        model.addAttribute("isOwner", byNickname.equals(account)); // 정보가 일치하면 프로필 주인임을 알 수 있음

        return "account/profile";
    }


    /**
     * 패스워드없이 로그인하기
     * &  패스워드 재설정
     */
    @GetMapping("/email-login")
    public String emailLoginForm() {
        return "account/email-login";
    }

    @PostMapping("/email-login")
    public String sendEmailLoginLink(String email, Model model, RedirectAttributes attributes) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            model.addAttribute("error", "유효한 이메일 주소가 아닙니다.");
            return "account/email-login";
        }

        if (!account.canSendConfirmEmail()) {
            model.addAttribute("error", "이메일 로그인은 1시간 뒤에 사용할 수 있습니다.");
            return "account/email-login";
        }

        accountService.sendLoginLink(account);
        attributes.addFlashAttribute("message", "이메일 인증 메일을 발송했습니다.");
        return "redirect:/email-login";
    }

    @GetMapping("/login-by-email")
    public String loginByEmail(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/logged-in-by-email";
        if (account == null || !account.isValidToken(token)) {
            model.addAttribute("error", "로그인할 수 없습니다.");
            return view;
        }

        accountService.login(account);
        return view;
    }


}
