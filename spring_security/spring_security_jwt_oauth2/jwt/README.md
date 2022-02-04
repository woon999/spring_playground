# Spring Security JWT 로그인 구현하기
## JWT(JSON Web Token)
[https://jwt.io/](https://jwt.io/)

JWT는 비밀(HMAC 알고리즘 사용) 또는 RSA 또는 ECDSA를 사용하는 공개/개인 키 쌍을 사용하여 서명할 수 있다.

JWT를 암호화여 당사자간에 비밀을 제공할 수 도 있지만 서명된 토큰에 중점을 둔다. 
서명된 토큰은 그 안에 포함된 클레임의 무결성을 확인할 수 있는 반면 암호화된 토큰은 다른 당사자로부터 이러한 클레임을 숨긴다. 
공개/개인 키 쌍을 사용하여 토큰에 서명할 때 서명은 개인 키를 보유한 당사자만이 서명한 사람임을 인증한다.

### HS256 방식
1. 클라이언트가 서버에게 (ID, PW)를 보내어 로그인 요청
2. 서버가 DB 데이터로 클라이언트 정보를 비교하여 인증 완료되면 토큰을 발급
    - 서버가 보유하고 있는 개인키(secret key)와 header, payload를 암호화해서 토큰 생성
    - header: HS256
    - payload: {username : user}
    - signature: (header + payload  + secret key)
    - 각 데이터 암호화 {hedaer}.{payload}.{signature} 후 전송
3. 클라이언트는 해당 토큰을 로컬 스토리지에 저장하여 유효기간이 만료될 때 까지 인증 절차를 받을 때 사용 가능
4. 서버는 jwt 토큰을 받으면 자신의 secret key를 대입하여 해당 토큰이 유효한 토큰인지 검증 후 인가     
 
### RSA 방식
RSA방식은 있는 이는 비대칭키 방식(공개키, 개인키)를 만들어서 사용
- 토큰 발급할 때는 개인키로 암호화해서 클라이언트에게 보냄
- 서버에서 다시 검증할 때는 공개키로 복호화하여 처리
- auth서버만 개인키를 갖고 있고, 다른 API Server는 공개키만 들고 있다.
- 보통 HS256을 더 선호함

<br>

## Spring Security 설정
### http.csrf().disable();

### http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

### .formLogin().disable()

### .httpBasic().disable()
쿠키 http only : false


### @CroosOrigin vs CorsFilter
인증 유무 차이
- 인증이 필요없을 때는 Controller단에 @CrossOrigin 설정하면 됨.
- 인증이 필요할 때는 시큐리티 필터에 CorsFilter 걸어줘야 함.

<br>

## Authorization 인증
쿠키는 서버가 많아지면 많아질수록 확장성에 안좋음. 그래서 header에 Authorization에 담아서 보내는데 Basic, Bearer인증 방식이 있음.

### Basic 인증
ID, PW를 직접 보내야하기 때문에 https 통신에서만 사용 가능
   
### Bearer 인증
(ID, PW) -> 토큰으로 변경하여 토큰을 통해 통신하는 방식
- 토큰이 노출이 되면 보안 위험있기는 마찬가지
- JWT 토큰이 주로 사용됨


<br>

## 서블릿 필터 체인


### 우선순위
- 시큐리티 필터가 먼저 실행
- 그 다음에 일반 필터들이 실행 (FilterRegistrationBean)

### 시큐리티 필터에 등록하기 SecurityConfig 
http.addFilterBefore(Filter filter, Class<? extends Filter> beforeFilter)
- 시큐리티 필터보다 먼저 실행

http.addFilterAfter(Filter filter, Class<? extends Filter> afterFilter)
- 시큐리티 필터가 모두 끝나고 나서 실행됨 

### 시큐리티 필터 체인
<img width="700" alt="스크린샷 2022-02-04 오후 2 24 16" src="https://user-images.githubusercontent.com/54282927/152476884-9292e1b2-9fa9-4c72-8778-ea1d5924d9cc.png">

<br>

## 로그인 처리 진행
### 시큐리티 필터 UsernamePasswordAuthenticationFilter을 이용하여 로그인 처리 진행
- /login으로 (username, password) POST 요청하면 동작됨 
- formLogin.disable()하면 동작안하지만, addFilter로 해당 필터 등록하면 따로 이 필터만 동작됨

### 동작 과정 attemptAuthentication
1. ObjectMapper를 이용하여 json 객체 User정보에 매핑해줌
2. UsernamePasswordAuthenticationToken에 (username, password)을 담음
3. authenticationManager로 authenticate 메서드에 해당 인증 요청 정보(authenticationToken)를 넣어 동작시킴
4. authenticationManager로 로그인 시도하면 PrincipalDetailsService가 호출되어 loadUserByUsername() 실행됨.
5. loadUserByUsername에서 DB에서 유저정보를 조회하여 정보가 존재하면 PrincipalDetails(User) 객체 반환
6. authentication객체가 담기면 로그인 완료되었다는 뜻
7. return authentication 객체 반환

리턴되면서 authentication 객체(로그인한 정보; 인증된 객체)가 session 영역에 저장됨
- jwt토큰을 사용하면 세션을 만들 이유는 없지만 리턴하는 이유는 권한 관리를 security가 대신 해주기 때문에 세션에 넣어줌

### 동작 과정 successfulAuthentication
attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행됨
- JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.

<br>

## 세션 vs 토큰
### 세션ID를 이용한 인증 
if username, password 로그인 정상
- 서버쪽 세션 ID 생성
- 클라이언트 쿠키 세션 ID를 응답

요청할 때 마다 쿠키값에 들어있는 세션 ID를 들고 서버쪽으로 요청하고 서버는 세션 ID가 유효한지 검증하고 접근권한에 대한 인가를 내림

<br>

### jwt 토큰을 이용한 인증
if username, password 로그인 정상
- 서버에서 jwt 토큰 생성
- 클라이언트 쪽으로 jwt 토큰을 응답

요청할 때 마다 헤더에 들어있는 jwt토큰을 가지고 요청하고 서버는 jwt토큰이 유효한지 검증하고 인가를 내림
- 그런데 jwt 토큰이 유효한지 판단하는 필터가 따로 없기 때문에 이에 대한 필터를 직접 만들어야 함 

### jwt 토큰 검증 필터 만들기

 

