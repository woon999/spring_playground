# 카오스 엔지니어링 

`넷플릭스(Netflix)의 카오스 엔지니어링을 설명하기 위한 기본 원칙을 담고 있는 PRINCIPLES OF CHAOS ENGINEERING의 내용을 참고하였습니다.` 

카오스 엔지니어링(Chaos Engineering)이란 프로덕션 서비스의 각종 장애 조건을 견딜 수 있는 시스템의 신뢰성을 확보하기 위해 분산 시스템을 실험하고 배우는 분야이다. 대규모 분산 소프트웨어 시스템 발전으로 인해 IT 업계는 발빠른 변화가 일어나고 있다. 개발 유연성과 배포 속도를 높이는 방식을 발빠르게 채택하고 있다. 이러한 방식에 대해 마주한 질문은 "우리가 배포 환경의 복잡한 시스템자체를 우리가 신뢰할 수 있는가?"이다.

분산 시스템 내부의 각 개별 서비스가 올바르게 작동하는 경우에도 해당 서비스 간의 상호 작용으로 인해 예측할 수 없는 결과가 발생할 수 있다. 즉, 프로덕션 환경에 직접 영향을 미치는 아주 드물지만 파괴적인 실제 장애로 인해 예기치 않은 결과가 발생하면, 분산 시스템이 혼란(Chaos)를 가져오게 된다.

 

예측 불가능한 비정상적인 행동이 시스템에 나타나기 전에 이러한 약점들을 식별할 수 있어야 한다. 

부적절한 fallback 설정 (어떤 기능이 약해지거나 제대로 동작하지 않을 때, 이에 대처하는 기능 또는 동작)
부적절한 타임 아웃 설정으로 폭풍 재시도 로직 발생
너무 많은 트래픽을 받는 다운 스트림 종속성
단일 실패 지점이 충돌할 때 연쇄 장애 
이 때, 고객에게 영향을 미치기 전에 중요한 문제를 사전에 해결해야 한다. 즉, 시스템에 내재된 혼란을 관리함으로서 유연상과 속도를 증가시키고, 복잡성이 존재하고 있더라도 프로덕션 배포에 대해 신뢰를 가져야 한다.

 

경험 및 시스템 기반 접근 방식(empirical, systems-based approach)은 대규모 분산 시스템의 혼란을 해결하고 이러한 시스템이 현실적인 조건을 견딜 수 있다는 확신을 심어준다. 통제된 실험을 하는 동안 관찰함으로써 분산 시스템의 동작에 대해 배운다. 우리는 이것을 카오스 엔지니어링(Chaos Engineering) 이라고 부르기로 했다

<br>

# Spring CM4SB 카오스 엔지니어링 
https://github.com/codecentric/chaos-monkey-spring-boot


<br>

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

<br>


## Jmeter로 CM4SB 응답 지연 테스트하기 
Jmeter로 GET post/1 테스트
- Thread: 20
- Ramp-up Period: 1
- Loop Count: infinite
<img width="1400" alt="스크린샷 2022-04-18 오후 10 55 32" src="https://user-images.githubusercontent.com/54282927/163818537-135af957-45c4-4592-9fea-8614b5fee46d.png">

<br>

### 응답지연 공격 설정 전
summary +  94584 in 00:00:30 = 3152.8/s Avg: 6 Min: 1 Max: 34 Err: 0 (0.00%) Active: 20 Started: 20 Finished: 0
### 응답지연 공격 설정 후
summary + 543 in 00:00:30 = 18.2/s Avg: 1110 Min: 1 Max: 4990 Err: 0 (0.00%) Active: 20 Started: 20 Finished: 0

<img width="1400" alt="스크린샷 2022-04-18 오후 10 50 38" src="https://user-images.githubusercontent.com/54282927/163818425-5f7aecb2-e710-430b-a977-576245aa3e18.png">


---

