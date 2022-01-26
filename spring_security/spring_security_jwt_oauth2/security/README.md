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

