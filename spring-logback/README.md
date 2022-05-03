# Logback 사용하기
- https://logback.qos.ch/
스프링 부트에선 기본적으로 Logback이 설정되어 있다. 

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

## 1. application.yml로 설정하기
~~~
logging:
  level:
    root: debug
~~~