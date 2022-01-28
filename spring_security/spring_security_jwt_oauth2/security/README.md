# Spring Security로 로그인 구현하기 (jwt, oauth2)

## 1. security 기본 로그인 구현
### securityConfig 환경설정
WebSecurityConfigurerAdapter를 상속받은 파일

### 회원가입
폼으로 유저 정보를 받아 저장한다. 단, 평문 패스워드는 저장 x
- 설정파일에서 BCryptPasswordEncoder 빈을 생성하여 입력받는 패스워드를 인코딩해서 저장한다.

### 로그인
시큐리티 설정 (.loginProcessingUrl("/login")) 진행
- /login 주소 요청이 오면 로그인 진행시킴
- 로그인 진행이 완료되면 시큐리티 전용 session에 유저 정보를 넣어줘야됨 (Security ContextHolder)

#### PrincipalDetails 클래스
Security ContextHolder에 넣는 유저 정보 타입은 Authentication 객체이어야 한다 
-> Authentication 객체 안에는 UserDetails타입으로 User정보를 받는다.
-> 그래서 우리는 User정보를 개 감싸서 내보내줘야 한다. 

#### PrincipalDetailsService
PrincipalDetails에서 PrincipalDetails 감싸준 User객체를 PrincipalDetailsService에서 반환한다.  
- 즉, 로그인된 유저 정보를 서비스단에서 UserDetails 객체로 반환


### 권한처리
시큐리티 설정 파일에 어노테이션 명시
- @EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)

@Secured, @PreAuthorize, @PostAuthorize(이건 잘 사용안함)
- @Secured는 예전버전, Pre,Post가 최신버전
- 차이점은 @PreAuthorize은  SpEL(Spring Expression Language)과 함께 작동이 가능
- https://stackoverflow.com/questions/3785706/whats-the-difference-between-secured-and-preauthorize-in-spring-security-3
- 그러니 최신버전을 사용하자 

Security Session 안에 => Authentication 안에 => UserDetails 타입 필요


<br>

## 2. OAuth2 로그인 구현
시큐리티 세션에 있는 Authentication 객체 안에 UserDetails, OAuth2User 객체를 저장할 수 있음
- UserDetails: 기존 로그인 유저 객체, OAuth2User: OAuth 로그인 유저 객체
- 그래서 PrincipalDetails에 UserDetails, OAuth2User 모두 구현해야 함

CustomOAuth2UserService loadUser(), PrincipalDetailsService loadUserByUsername()
- loadUser, loadUserByUsername에서 각각 로그인 유저 객체를 시큐리티 session에 있는 ([UserDetails로 포장된] Authentication 객체)로 보내준다.
- return PrincipalDetails -> Authentication 객체에 저장 
- 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
- loadUserByUsername(): loginForm으로 부터 받은 user 데이터에 대한 후처리되는 함수 (return new PrincipalDetails(user);) // UserDetails
- loadUser(): 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수 (return new PrincipalDetails(user, oAuth2User.getAttributes());)  // OAuth2User

<br> 

### OAuth2 유저 객체 
#### oauth2 google user
- username = google_{sub}
- password = "암호화(겟인데어)" > 의미없음
- email = {email}
- role = "ROLE_USER"
- provider = "google"
- providerId = {sub}

<br> 

### @AuthenticationPrincipal
@AuthenticationPrincipal 어노테이션으로 Authentication에 저장되어 있는 일반, OAuth 로그인 유저 객체 모두 받아올 수 있음
- authentication.getPrincipal() == PrincipalDetails 객체
- authentication.getPrincipal().getUser() == @AuthenticationPrincipal달린 principalDetails.getUser()
~~~
@GetMapping("/user")
public @ResponseBody String testLogin(Authentication authentication){
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    System.out.println("User : " + principalDetails.getUser());
    return "user";
}

@GetMapping("/user")
public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    System.out.println("User : " + principalDetails.getUser());
    return "user";
}
~~~