# testContainer로 테스트환경 구축하기
## 1. 로컬 환경 구성
- docker-compose로 spring과 mysql을 실행
~~~
version: '3.1'

services:
  mysql:
    image: mysql
    container_name: mysqldb
    environment:
      - MYSQL_DATABASE=foodb
      - MYSQL_ROOT_PASSWORD=foo
    ports:
      - 3307:3306
    networks:
      - spring-net

  spring-testcontainers-app:
    image: spring-testcontainers-app
    container_name: spring-testcontainers-app
    build: .
    restart: always
    environment:
      MYSQL_HOST: mysqldb
      MYSQL_DATABASE: foodb
      MYSQL_USER: root
      MYSQL_PASSWORD: foo
      MYSQL_PORT: 3306
    ports:
      - 8080:8080
    depends_on:
      - mysql
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
testImplementation "org.testcontainers:mysql:1.16.3"
~~~ 

maven
~~~
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>testcontainers</artifactId>
    <version>1.16.3</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>1.16.3</version>
    <scope>test</scope>
</dependency>
~~~

<br>

## mysql testContainers 생성
1. @SpringBootTest 통합테스트 클래스(CustomerIntegrationTest)를 생성한다.
2. @TestContainers 애노테이션을 추가한다.
3. "mysql:8"이미지를 추가하여 MySQLContainer 인스턴스를 생성한다.
    - [mysql 이미지 참고](https://hub.docker.com/_/mysql)
    - container가 모든 단위테스트마다 생성되지 않도록 static은 필수

### 로컬 DB 설정과 매핑
test 프로필 설정을 따로 해주지 않으면 application.yml 설정을 참고한다. 그래서 test db 설정도 그와 일치하게 되므로 container 값도 그와 똑같이 설정해준다.

application.yml
~~~
spring:
  datasource:
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:test}
    username: ${MYSQL_USER:test_user}
    password: ${MYSQL_PASSWORD:1234}
~~~

<br>

CustomerIntegrationTest
- @DynamicPropertySource로 jdbcUrl은 testContainer로 오버라이딩해준다. 
~~~
@Transactional
@SpringBootTest
@Testcontainers
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Container
	private static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8")
					.withDatabaseName("test")
					.withUsername("test_user")
					.withPassword("1234");

        @DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
	
	}
	@Test
	// ...

}
~~~

<br>

### 테스트 독립 환경 배포
다음과 같이 컨테이너 자체에 있는 디폴트 값 설정을 오버라이딩하면 굳이 로컬 DB 설정과 매핑을 시켜주지 않아도 된다.
- MysqlContainer 기본값 (databaseName = "test", user="test", password="test")
    - 참고: https://github.com/testcontainers/testcontainers-java/blob/master/modules/mysql/src/main/java/org/testcontainers/containers/MySQLContainer.java 
- @DynamicPropertySource로 다음 값을 오버라이딩하여 실행하면 docker를 새로 띄워서 mysql 컨테이너가 정상적으로 동작한다.
~~~
@Transactional
@SpringBootTest
@Testcontainers
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Container
	private static MySQLContainer mysqlContainer = new MySQLContainer("mysql:8");


	@DynamicPropertySource
	public static void overrideProps(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mysqlContainer::getUsername);
		registry.add("spring.datasource.password", mysqlContainer::getPassword);
	}


	@Test
	// ...
}

~~~


<br>

### @ActiveProfiles("test") 애노테이션 사용하기
testcontainers mysql 8은 `jdbc:tc:mysql:8:///`로 url을 설정해주면 된다.
- mysql settings : ex. jdbc:tc:mysql:5.7.34://hostname/databasename?TC_MY_CNF=somepath/mysql_conf_override
    - 참고: https://www.testcontainers.org/modules/databases/mysql/ 
~~~
spring:
  datasource:
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    url: jdbc:tc:mysql:8:///
~~~

~~~
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	// ...
}
~~~

<br>

### 독립적인 test db 환경 테스트하기
testContainers가 정상적으로 동작하니 이젠 test에서만 사용할 수 있는 test db를 만들어주면 된다.
1. test data 값을 /test/resources/schema.sql 파일에 입력한다.
2. 실행하면 로컬에서 실행되고 있는 DB와 다른 데이터 출력되는 것을 확인할 수 있다.
3. local은 ('jong', 'lee') ... , test는 ('test', 'lee') ...

appliction-test.yml에 설정 추가
~~~
spring:
  sql:
    init:
      mode: always  
~~~

/test/resources/schema.sql
~~~
DROP TABLE IF EXISTS customers;
create table customers(id BIGINT not null auto_increment primary key, first_name VARCHAR(255), last_name VARCHAR(255));
insert into customers (first_name, last_name) values ('test', 'lee');
insert into customers (first_name, last_name) values ('test', 'kim');
insert into customers (first_name, last_name) values ('test2', 'lee');
~~~


<br>

 
## TestContainers 정보 스프링 테스트에서 사용하기
#### @ContextConfiguration
- 스프링 테스트 컨텍스트가 사용할 설정 파일 또는 컨텍스트를 커스터마이징할 수 있는 방법을 제공한다.

#### ApplicationContextInitializer
- 스프링 ApplicationContext를 프로그래밍으로 초기화 할 때 사용할 수 있는 콜백 인터페이스로, 특정 프로파일을 활성화 하거나, 프로퍼티 소스를 추가하는 등의 작업을 할 수 있다.

#### TestPropertyValues
- 테스트용 프로퍼티 소스를 정의할 때 사용한다.

~~~
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = CustomerIntegrationTest2.ContainerPropertyInitializer.class)
class CustomerIntegrationTest2 {

	@Value("${container.port}") int port;

	@Container
	private static GenericContainer container = new GenericContainer("mysql:8")
		.withExposedPorts(3307);

	@Test
	void get_container_mapped_port_by_3307() {
		System.out.println("port = " + port);
	}

	static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			TestPropertyValues.of("container.port=" + container.getMappedPort(3307))
				.applyTo(applicationContext);
		}
	}
}
~~~
 
 
<br>

## TestContainers 싱글톤 컨테이너로 Mysql 연동하여 통합 및 단위 테스트하기  
때로는 여러 테스트 클래스에 대해 한 번만 시작되는 컨테이너를 정의하는 것이 유용할 수 있습니다. 
Testcontainers 확장에서 제공하는 이 사용 사례에 대한 특별한 지원은 없습니다. 대신 다음 패턴을 사용하여 구현할 수 있습니다.

<br>

### 통합테스트 @SpringBootTest기 커스텀하기 
#### 1. AbstractContainerBaseTest 추상 클래스를 생성해서 통합 테스트하는 클래스에 상속해준다.
- 그런데 profile: test만 매핑되어도 testContainers db 연동되어 실행됨. 따로 container.start()없이 그래도 일단 명시적으로 표기는 해둠.  
~~~
public abstract class AbstractContainerBaseTest {
	static final String MYSQL_IMAGE = "mysql:8";
	static final MySQLContainer MY_SQL_CONTAINER;
	static {
		MY_SQL_CONTAINER = new MySQLContainer(MYSQL_IMAGE);
		MY_SQL_CONTAINER.start();
	}
}
~~~

<br>

#### 2. 통합 테스트 애노테이션을 따로 커스텀한다. 
- @ActiveProfiles("test")로 test 프로필 매핑, @Transactional로 테스트 롤백 처리
~~~
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
public @interface IntegrationTest {
}
~~~

<br>

#### 3. 통합 테스트를 하는 클래스에 @IntegrationTest 애노테이션 명시 및 AbstractContainerBaseTest 추상클래스 상속
- profile test로 설정 -> application-test.yml에 명시된 tc db 실행되어 동작
- AbstractContainerBaseTest 싱글턴 컨테이너로 실행

##### 참고
- 싱글톤 컨테이너는 기본 클래스가 로드될 때 한 번만 시작됩니다. 
그러면 컨테이너를 상속하는 모든 테스트 클래스에서 사용할 수 있습니다. 
테스트 스위트의 끝에서 Testcontainers 코어에 의해 시작된 Ryuk 컨테이너는 싱글톤 컨테이너 중지를 처리합니다 .
- https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control/#singleton-containers
 
~~~
@IntegrationTest
class FooIntegrationTest1 extends AbstractContainerBaseTest {
}

..

@IntegrationTest
class FooIntegrationTest2 extends AbstractContainerBaseTest {
}

..

@IntegrationTest
class FooIntegrationTest3 extends AbstractContainerBaseTest {
}
~~~
 
 
<br>

### 단위 테스트 @DataJapTest 커스텀하기
- AutoConfigureTestDatabase None: @DataJpaTest에서 자동으로 매핑되는 테스트 디비 설정 None으로 변경. TC 컨테이너 디비와 매핑해줄 것이기 때문 
~~~
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public @interface TCDataJpaTest {
}
~~~

~~~
@TCDataJpaTest
class CustomerRepositoryTest extends AbstractContainerBaseTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
        // ...	
}
~~~