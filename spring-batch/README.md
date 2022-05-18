# 스프링 배치 Spring Batch 사용하기
## 1. 기본 라이브러리 설정
- Spring Batch, JPA, Mysql, H2, Lombok 사용
<img width="500" alt="스크린샷 2022-05-18 오후 4 51 00" src="https://user-images.githubusercontent.com/54282927/168987157-5c63f912-c8ac-438b-94cf-7d05ceece770.png">

<br>

~~~
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-batch'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	runtimeOnly 'mysql:mysql-connector-java'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.batch:spring-batch-test'
}
~~~

<br>

## 2. Spring Batch Job 생성 후 실행하기
### 2-1. @EnableBatchProcessing 배치 기능 활성화하기
이 애노테이션은 Spring Batch를 사용하기 위해서는 필수로 명시해줘야 한다. 
~~~
@EnableBatchProcessing // 배치 기능 활성화
@SpringBootApplication
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}
~~~

<br>

### 2-2. Simple Job 생성하기
Job은 하나의 배치 작업 단위이다. Job 안에는 여러 Step이 존재하고, Step 안에는 Tasklet 혹은 Reader & Processor & Writer 묶음이 존재한다.

<img width="550" alt="스크린샷 2022-05-18 오후 5 18 15" src="https://user-images.githubusercontent.com/54282927/169000196-b9907590-8da1-4439-8c83-5de5eb539e9d.png">

<br>

해당 코드는 Batch Job을 생성하는 `simpleJob`은 `simpleStep1`을 품고있다. 
~~~
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimpleJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job simpleJob(){
		return jobBuilderFactory.get("simpleJob") // "simpleJob"이름을 가진 Batch Job 생성
			.start(simpleStep1())
			.build();
	}

	// Batch가 수행되면 log를 출력하는 step 생성
	@Bean
	public Step simpleStep1(){
		return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step1 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
~~~


<br>

### 실행 결과 
<img width="1680" alt="스크린샷 2022-05-18 오후 5 55 22" src="https://user-images.githubusercontent.com/54282927/169000211-25d59b6f-4f2b-4e82-af10-879aa28fdda8.png">


<br>

## 3. DB 사용하기
### 3-1. 프로필 별로 application.yml 설정하기
#### application-local.yml
Local 환경은 h2 인메모리 DB를 사용한다. 
~~~
spring:
  datasource:
    hikari:
      jdbc-url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
      username: sa
      password:
      driver-class-name: org.h2.Driver
~~~

#### applicatiion-real.yml
real 배포 환경에서는 mysql을 사용한다.
~~~
spring:
  datasource:
    hikari:
      jdbc-url: jdbc:mysql://localhost:3306/batch_db
      username: ${DB_USERNAME}
      password: ${DB_PASSWORD}
      driver-class-name: com.mysql.cj.jdbc.Driver
~~~

<br>

#### application-real.yml
~~~
spring:
  datasource:
    hikari:
      jdbc-url: jdbc:mysql://localhost:3306/batch_db
      username: test_user
      password: 1234
      driver-class-name: com.mysql.jdbc.Driver
~~~

<br> 

### Mysql schem-mysql.sql
그냥 실행하면 에러가 발생한다. Spring Batch의 기본 도메인 테이블을 미리 생성해줘야 한다.  
- [shift + shift] -> `mysql-schema.sql` 검색 -> Mysql DB에 접속하여 해당 쿼리 실행
- 또는 yml에 아래와 같이 설정하면  `mysql-schema.sql`가 자동으로 실행되어 DB 테이블으 생성해준다.
~~~
spring:
  batch:
    jdbc:
      initialize-schema: always
~~~  

### Batch 테이블 생성
<img width="414" alt="스크린샷 2022-05-19 오전 1 00 45" src="https://user-images.githubusercontent.com/54282927/169092352-a9e080dd-2aee-4200-b9ed-60793ebfa091.png">


<br>


### Mysql 연동 성공 
그리고 앱을 실행하면 다음과 같이 정상적으로 Job이 실행되는 것을 확인할 수 있다.

<img width="1657" alt="스크린샷 2022-05-19 오전 1 18 00" src="https://user-images.githubusercontent.com/54282927/169092361-fff36f96-45a0-4e8b-9463-5e6cb132e47b.png">

