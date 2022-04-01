
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
4. MySQLContainer에 (database=test, username=test, password=test) 값을 넣어줘 생성한다. 
5. 실행하면 docker를 새로 띄워서 mysql 내부에 있는 데이터가 정상적으로 동작한다.
~~~

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CustomerIntegrationTest {

	@Autowired
	private CustomerRepository customerRepository;

	@Container
	private static MySQLContainer container = new MySQLContainer("mysql:8");

	static {
		container.start();
	}

	@Test
	void schema_script_data_should_be_two() {
		List<Customer> customers = customerRepository.findAll();
		customers.forEach(System.out::println);
		assertEquals(customers.size(), 2);
	}

	@Test
	void when_using_a_clean_db_this_should_be_empty() {
		customerRepository.deleteAll();
		List<Customer> customers = customerRepository.findAll();
		System.out.println(customers.size());
		assertEquals(customers.size(), 0);
	}
}
~~~

<br>

### @ActiveProfiles("test") 애노테이션 추가
- MysqlContainer 기본값 (databaseName = "test", user="test", password="test")
    - 참고: https://github.com/testcontainers/testcontainers-java/blob/master/modules/mysql/src/main/java/org/testcontainers/containers/MySQLContainer.java

#### container 값 설정하기
- 그런데 사실상 이러한 설정값이 무의미한게 아무값이나 입력되어도 컨테이너는 다 정상적으로 작동
~~~
@Container
private static MySQLContainer container = new MySQLContainer("mysql:8")
    .withDatabaseName("testdb")
    .withPassword("1234");
~~~

<br>

- mysql settings : jdbc:tc:mysql:5.7.34://hostname/databasename?TC_MY_CNF=somepath/mysql_conf_override
    - mysql 8 :  jdbc:tc:mysql:8:///
    - 참고: https://www.testcontainers.org/modules/databases/mysql/
~~~
spring:
  datasource:
    driver-class-name: org.testcontainers.jdbc.ContainerDatabaseDriver
    url: jdbc:tc:mysql:8://testdb/
    username: test
    password: 1234
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
  sql:
    init:
      mode: always
~~~

<br>

### 독립적인 test db 환경 테스트하기
testContainers가 정상적으로 동작하니 이젠 test에서만 사용할 수 있는 test db를 만들어주면 된다.
1. test data 값을 /test/resources/schema.sql 파일에 입력한다.
2. 실행하면 로컬에서 실행되고 있는 DB와 다른 데이터 출력되는 것을 확인할 수 있다.
3. local은 ('jong', 'lee') ... , test는 ('test', 'lee') ...
~~~
DROP TABLE IF EXISTS customers;
create table customers(id BIGINT not null auto_increment primary key, first_name VARCHAR(255), last_name VARCHAR(255));
insert into customers (first_name, last_name) values ('test', 'lee');
insert into customers (first_name, last_name) values ('test', 'kim');
insert into customers (first_name, last_name) values ('test2', 'lee');
~~~


<br>


 
 
 
 

