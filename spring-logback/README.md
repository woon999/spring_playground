# Logback 사용하기
- https://logback.qos.ch/
스프링 부트에선 기본적으로 Logback이 설정되어 있다. 

<br>

## log 출력하는 로직 만들기
- 로그 레벨은 기본적으로 [trace > debug > info > warn > error] 순이다.
- default설정은 info이다.
~~~
/**
 * trace < debug < info < warn < error
 * default : info
 */
@RestController
public class FooController {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@GetMapping("/")
	public void log(){
		log.trace("trace message");
		log.debug("debug message");
		log.info("info message"); // default
		log.warn("warn message");
		log.error("error message");
	}
}

// spring-boot-starter-web 의존성을 추가했다면 @Slf4j 애노테이션으로 바로 log 사용이 가능하다.
@RestController
@Slf4j
public class FooController {
	@GetMapping("/")
	public void log(){
		log.trace("trace message");
		log.debug("debug message");
		log.info("info message"); // default
		log.warn("warn message");
		log.error("error message");
	}
}
~~~

<br>

## 1. application.yml로 설정하기
이는 로컬 개발 환경에서 설정하기에 적당하다.
왜냐하면 운영을 하게되면 각 로컬, 개발, 운영 환경마다 로그 설정을 다르게 해주어야 하는데 yml 파일로 일일이 설정하기에는 번거롭기 때문이다. (불가능한 것은 아)
~~~
logging:
  level:
    root: debug
~~~

<br>

## 2. logback-spring.xml
그래서 logback-spring.xml 파일을 생성하여 별도로 로그를 관리하는 방식을 사용하면 좋다. xml 설정은 appender와 logger로 나눌 수 있다.
- appender: logger를 어디에 출력할지 설정한다. 콘솔, 파일, DB 등 지정할 수 있다.
- logger: 아래 예제는 매우 단순한 형태로 실제 업무에선 FILE 뿐만 아니라 SockerAppender나 LogStash 등도 함께 설정해서 사용할 수 있다.
- [로깅 패턴 참고](https://logback.qos.ch/manual/configuration.html) 

다음 xml 설정은 콘솔에는 모든 로그를 debug 수준의 로그만 출력되도록 설정한 것으로 위 application.yml 설정과 동일하다.
~~~
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
<!-- 콘솔(STDOUT)에 log 기록 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{36} - %msg%n</Pattern>
        </layout>
    </appender>

<!-- log root 레벨 설정 (logging.level.root=info)-->
    <root level="debug">
<!--     참조할 appender 설정 - STDOUT -->
        <appender-ref ref="STDOUT" />
    </root>

</configuration>
~~~

<br>

## 콘솔과 파일에 로그 설정 분리하여 기록하기
RollingFileAppender는 FileAppender를 상속하여 로그 파일을 rollover한다. rollover는 다음 파일로 이동하는 행위로 한 로그 파일에 무한정 기록할 수 없으니 특정 기준에 따라 기록하는 파일 대상을 바꿔주는 것이다. 해당 appender는 주요 2가지 설정을 해줘야 한다.
- RollingPolicy: rollover에 필요한 action을 설정한다. (ex. TimeBasedPolicy, SizeAndTimeBasedRollingPolicy 등)
- TriggeringPolicy: rollover가 발생하는 기준(정책)을 설정한다.

다음 xml 파일은 콘솔에 모든 로그를 info 수준에서 출력해주고 '/logs/loback.log' 파일에 springframework.web 로그를 debug 수준으로 기록한다. 그리고 rollover 정책은 파일 크기와 시간(SizeAndTimeBased)을 기준으로 설정되어 있다.
- fileNamePattern: yyyy-mm-dd로 일 단위로 로그를 기록한다.
- Size: 로그 파일 아카이브 저장소는 최대 3GB로 이를 초과한다면 가장 오래된 파일이 삭제된다. 
- Time: 최대 30일의 히스토리가 남는다. 30일 이상의 파일이 기록될시 가장 오래된 파일이 삭제된다.
~~~
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
<!-- log 기록 절대 위치 설정 -->
    <property name="LOGS_ABSOLUTE_PATH" value="./logs" />

<!-- 콘솔(STDOUT)에 log 기록 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{36} - %msg%n</Pattern>
        </layout>
    </appender>

<!-- log root 레벨 설정 (logging.level.root=info)-->
    <root level="info">
<!--     참조할 appender 설정 - STDOUT -->
        <appender-ref ref="STDOUT" />
    </root>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
<!--     log 기록할 파일 위치 설정 -->
        <file>${LOGS_ABSOLUTE_PATH}/logback.log</file>
<!--     log 기록 타입 인코딩 -->
        <encoder>
            <pattern>[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{35} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- daily rollover -->
            <fileNamePattern>logFile.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- keep 30 days' worth of history capped at 3GB total size -->
            <maxHistory>30</maxHistory>
            <totalSizeCap>3GB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <logger name="org.springframework.web" level="debug">
        <appender-ref ref="FILE" />
    </logger>

</configuration>
~~~

<br>

## Spring 앱 Profile에 따라 로그 기록 설정하기
실수로 다른 환경에 로그 기록이 덮어씌워지거나 갱신되는 일을 방지하고자 프로젝트 운영시 각 실행 환경에 따라 로그 전략을 다르게 설정해줘야 한다. sping에서 실행된 profile에 따라 조건들을 xml에서 설정할 수 있다. 다음은 spring profile이 local, dev, real 환경에 따라 서로 다른 로그를 출력하는 xml 설정 파일이다.


<br>

---