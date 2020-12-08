package com.study.Jpawebapp.account;

import com.study.Jpawebapp.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        //이메일 인증 토큰 생성
        newAccount.generateEmailCheckToken();

        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }


    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdatedByWeb(true)
                .build();

        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        //이메일 전송
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("닷 스터디, 회원 가입 인증");
        mailMessage.setText("/check-email-token?token="+ newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);

        //이메일 토큰 내용과 시간 재설정
        newAccount.generateEmailCheckToken();
    }

    public void login(Account account) {
        // 원래 AuthenticationManager내부에서 사용하는 생성자
        // 정석정인 방법이 아닌 아래와 같이 코딩한 이유는 현재 인코딩한 패스워드밖에 접근하지 못하기 때문
        // 정석적인 방법인 플레인 텍스트로 받은 pw를 써야하는데 현재 db에 저장도 안하고 쓸 일도 없기 때문
        log.info("principal : "+  new UserAccount(account));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);

        /**
         * 원래 정석적인 방법
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username, passsword);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
         */

    }

    /**
     * DB에 있는 user정보 조회
     */
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(emailOrNickname);
        if(account == null){
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if(account == null){
            throw new UsernameNotFoundException(emailOrNickname);
        }

        //User Principal 반환
        return new UserAccount(account);
    }
}
