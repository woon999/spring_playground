# testContainer로 Redis 테스트 환경 구축하기 
## 1. 로컬 환경 구성
- docker-compose로 spring과 redis 실행
~~~
version: '3.1'

services:

  redis:
    image: redis
    container_name: rediscache
    ports:
    - 6380:6379
    networks:
      - spring-net

  spring-testcontainers-app:
    image: spring-testcontainers-app
    container_name: spring-testcontainers-app
    build: .
    restart: always
    environment:
      REDIS_HOST: rediscache
      REDOS_PORT: 6379
    ports:
      - 8080:8080
    depends_on:
      - redis
    networks:
      - spring-net

networks:
  spring-net:
~~~

<br>

## 2. testcontainers 설정 추가
- https://www.testcontainers.org/quickstart/junit_5_quickstart/

gradle
~~~
// testContainers
testImplementation "org.junit.jupiter:junit-jupiter:5.8.1"
testImplementation "org.testcontainers:junit-jupiter:1.16.3"
~~~ 


<br>

## redis testContainers 생성
### 1. @IntegrationTest 애노테이션 커스텀
통합테스트 @SpringBootTest와 "test" profile에서 실행하도록 하는 애노테이션을 만들어준다.
~~~
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@SpringBootTest
public @interface IntegrationTest {
}
~~~

### 2. 로컬 설정과 매핑
test 프로필 설정을 따로 해주지 않으면 application.yml 설정을 참고한다. 그래서 test db 설정도 그와 일치하게 되므로 container 값도 그와 똑같이 설정해준다.
- test에는 따로 설정해주지 않는다.

application.yml
~~~
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
~~~

### 3.AbstractContainerBaseTest 클래스 생성
- Redis 이미지를 싱글턴 인스턴스로 생성한다.
- 6379 포트와 매핑한다. 
- 그렇게 생성된 컨테이너 host, port 정보를 오버라이딩한다. (test.yml -> .yml -> RedisConfig -> connectionFactory(host, port) 적용)
~~~
public abstract class AbstractContainerBaseTest {
	static final String REDIS_IMAGE = "redis:6-alpine";
	static final GenericContainer REDIS_CONTAINER;

	static {
		REDIS_CONTAINER = new GenericContainer<>(REDIS_IMAGE)
			.withExposedPorts(6379)
			.withReuse(true);
		REDIS_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry){
		registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
		registry.add("spring.redis.port", () -> ""+REDIS_CONTAINER.getMappedPort(6379));
	}
}
~~~~

<br>

## 4. 통합 테스트 진행 CartControllerTest
컨테이너로 띄워진 redis로 정상적으로 테스트가 실행되는 것을 확인할 수 있다.
만약 위의 @DynamicPropertySource를 없애주면 로컬:6379 redis로 테스트 된다.
~~~
@IntegrationTest
class CartControllerTest extends AbstractContainerBaseTest {

	@Autowired
	private CartDao cartDao;

	@Test
	void addCartItem(){
	 //...
	}
}
~~~


<br>