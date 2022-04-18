# Spring CM4SB 카오스 엔지니어링 
https://codecentric.github.io/chaos-monkey-spring-boot/latest/#getting-started

운영 중인 어플리케이션에선 항상 장애가 발생한다. 이러한 장애를 미리 테스트해서 방지하는 걸 카오스 엔지니어링이라고 한다.

## gradle 의존성 추가
- https://codecentric.github.io/chaos-monkey-spring-boot/latest/#_using_external_dependency_jar_file
~~~
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'de.codecentric:chaos-monkey-spring-boot:2.5.4'
~~~

<br>

## Jmeter 파일 저장 후 실행   
$ ~/apache-jmeter-5.4.3/bin/jmeter -n -t ./All.jmx
~~~
summary = 3756 in 00:01:17 = 48.5/s Avg:408 Min:141 Max:658 Err:0 (0.00%)
~~~

<br>

## Spring Boot Actuator Endpoints 설정 
### Chaos Monkey Endpoint 설정 
~~~
management:
  endpoint:
    chaosmonkey:
      enabled: true
    chaosmonkeyjmx:
      enabled: true
  endpoints:
    web:
      exposure:
        # include specific endpoints
        include:
          - health
          - info
          - chaosmonkey
~~~

<br>

## CM4SB 응답 지연
#### Repository Watcher 활성화
~~~
chaos:
  monkey:
    watcher:
      repository: true
~~~


#### Chaos Monkey 활성화
httpie 명령어 사용 brew install httpie
~~~
$ http post localhost:8080/actuator/chaosmonkey/enable
~~~

#### Chaos Monkey 활성화 확인
~~~
$ http localhost:8080/actuator/chaosmonkey/status
~~~

#### Chaos Monkey Watcher 확인
~~~
$ http localhost:8080/actuator/chaosmonkey/watchers
~~~

#### Chaos Monkey 지연 공격 설정
~~~
$ http post localhost:8080/actuator/chaosmonkey/assaults level=3 latencyRangeStart=2000 latencyRangeEnd=5000 latencyActive=true
~~~
- level 3: 3번 요청에 1번 공격 
- latencyRangeStart=2000 latencyRangeEnd=5000: 2초부터 5초내로 응답 지연시키기  
- latencyActive=true: latency 어택 활성화 
