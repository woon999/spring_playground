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
<img width="1000" alt="스크린샷 2022-05-18 오후 5 55 22" src="https://user-images.githubusercontent.com/54282927/169000211-25d59b6f-4f2b-4e82-af10-879aa28fdda8.png">


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

<br>

#### application-real.yml
real 배포 환경에서는 mysql을 사용한다.

~~~
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/batch_db
    username: test_user
    password: 1234
    driver-class-name: com.mysql.cj.jdbc.Driver
~~~

<br> 

### Mysql schem-mysql.sql으로 테이블 생성하기
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

<img width="1000" alt="스크린샷 2022-05-19 오전 1 18 00" src="https://user-images.githubusercontent.com/54282927/169092361-fff36f96-45a0-4e8b-9463-5e6cb132e47b.png">

<br>

## 3. Batch 메타 테이블
Spring Batch에 제공하는 메타 테이블들에 대해 알아보자.

<img width="700" alt="스크린샷 2022-05-19 오전 1 25 27" src="https://user-images.githubusercontent.com/54282927/169093663-c4a01171-6cba-427b-a3ed-c39645830146.png"> 

<br>

### 3-1. BATCH_JOB_INSTANCE 테이블
Job Instance 테이블은 Job Parameter에 따라 생성되는 테이블이다. Job Paramter란 Spring Batch가 실행될 때 외부에서 받을 수 있는 파라미터이다.
- 예를 들어, Job Parameter로 넘기면 Spring Batch에서는 해당 날짜 데이터로 조회/가공/입력 등의 작업을 할 수 있다.

<br>

해당 테이블을 보면 방금 실행했던 "simpleJob"을 확인할 수 있다. 
- `JOB_INSTANCE_ID`: BATCH_JOB_INSTANCE 테이블의 PK
- `JOB_NAME`: 수행한 Batch Job Name
- `JOB_KEY`: 동일한 Job이름의 JobInstance는 Job의 실행시점에 부여되는 고유한 JobParameter의 값을 통해 식별된다. 
             그리고 이렇게 식별되는 값의 직렬화(serialization)된 결과를 JOB_KEY라는 값으로 기록된다.

<img width="1000" alt="스크린샷 2022-05-19 오전 1 28 38" src="https://user-images.githubusercontent.com/54282927/169094276-0b4c02f8-2c24-4eb2-8476-43ccfe1e3bbe.png">

같은 Batch Job 이라도 Job Parameter가 다르면 Batch_JOB_INSTANCE에는 기록되며, Job Parameter가 같다면 기록되지 않는다.
이를 확인해보기 위해 Job의 설정 값을 다음과 같이 바꿔보자.
- @JobScope와 @StepScope는 스프링의 기본 Scope인 싱글톤 방식과는 대치되는 역할이다.
- Bean의 생성 시점이 스프링 애플리케이션이 실행되는 시점이 아닌 @JobScope, @StepScope가 명시된 메서드가 실행될 때까지 지연시키는 것을 의미한다. 
이러한 행위를 Late Binding이라고도 한다.
~~~
@Bean
public Job simpleJob(){
    return jobBuilderFactory.get("simpleJob") // "simpleJob"이름을 가진 Batch Job 생성
        .start(simpleStep1(null))
        .build();
}

@Bean
@JobScope
public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
    return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이름을 가진 Batch Step 생성
        .tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
            log.info(">>>>> step1 process");
            log.info(">>>>> request Date: {}", requestDate);
            return RepeatStatus.FINISHED;
        }))
        .build();
}
~~~


<br>

### 실행 결과 
이를 실행해보면 다음과 같이 정상적으로 동작하여 새로운 Job Instance가 생성되는 것을 확인할 수 있다. (Program arguments에 "requestDate=20220519" 입력)
만약 재실행한다면 중복되는 Job Instance는 생성이 불가능하기 때문에 에러가 발생할 것이다.
- 즉, 동일한 Job이 Job Parameter가 달라지면 그때마다 BATCH_JOB_INSTANCE에 생성되며, 동일한 Job Parameter는 여러개 존재할 수 없다.


<img width="700" alt="스크린샷 2022-05-19 오전 2 25 22" src="https://user-images.githubusercontent.com/54282927/169105646-cde0eaad-78c7-42a4-927a-54a34130c34b.png">

<img width="700" alt="스크린샷 2022-05-19 오전 2 30 46" src="https://user-images.githubusercontent.com/54282927/169105632-9092b843-337c-4bf1-85c2-b5adb3457d60.png">

<img width="1000" alt="스크린샷 2022-05-19 오전 2 13 26" src="https://user-images.githubusercontent.com/54282927/169105654-22dfb337-235f-4b2b-8e5b-7c59a388d00c.png">

<br>

마지막으로 Program arguments에 "requestDate=20220520" 입력해보면 정상적으로 입력되는 것을 확인할 수 있다. 

<img width="1000" alt="스크린샷 2022-05-19 오전 2 43 02" src="https://user-images.githubusercontent.com/54282927/169107903-a1266ddc-f512-41d3-aa2a-c4f35d8663e5.png">

<br>

### 3-2. BATCH_JOB_EXECUTION 테이블
먼저 테이블을 살펴보면 3개의 row를 발견할 수 있다. 위에서 job 인스턴스 (20200519, 20200520, 20200521) 3개의 실행 데이터이다.
- JOB_INSTANCE와 별 다를게 없어보이는데, 이는 JOB_EXECUTION와 JOB_INSTANCE는 부모-자식 관계이기 때문이다.
- JOB_EXECUTION은 자신의 부모 JOB_INSTACNE가 성공/실패했던 모든 내역을 갖고 있다.

<br>

simpleJob 코드를 다음과 같이 변경해보자.
~~~
@Bean
public Job simpleJob(){
    return jobBuilderFactory.get("simpleJob") // "simpleJob"이름을 가진 Batch Job 생성
        .start(simpleStep1(null))
        .next(simpleStep2(null))
        .build();
}

@Bean
@JobScope
public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
    return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이름을 가진 Batch Step 생성
        .tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
            // log.info(">>>>> step1 process");
            // log.info(">>>>> request Date: {}", requestDate);
            // return RepeatStatus.FINISHED;
            
            throw new IllegalStateException("step1에서 실패합니다.");
        }))
        .build();
}

@Bean
@JobScope
public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){
    return stepBuilderFactory.get("simpleStep2") // "simpleStep1"이름을 가진 Batch Step 생성
        .tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
            log.info(">>>>> step2 process");
            log.info(">>>>> request Date: {}", requestDate);
            return RepeatStatus.FINISHED;
        }))
        .build();
}
~~~

<br>

Program arguments에 "requestDate=20220521" 입력한 다음 실행하면 실패한 것을 볼 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오전 2 47 23" src="https://user-images.githubusercontent.com/54282927/169108750-1a268495-388b-4c0d-87d2-75c18c89a4c1.png">

<br>

그러면 이 실패한 기록이 BATCH_JOB_EXECUTION에도 저장이 된다. 

<img width="1000" alt="스크린샷 2022-05-19 오전 2 49 44" src="https://user-images.githubusercontent.com/54282927/169109662-a2e51467-598e-40ff-b21f-18c3cdb01181.png">

<br>

다시 원래대로 코드를 복구시키고 실행하면 정상적으로 동작이 되고 BATCH_JOB_EXECUTION에 5번째 새로운 컬럼이 생성된다. 
여기가 바로 JOB_INSTANCE와 결정적인 차이이다. BATCH_JOB_EXECUTION의 JOB_INSTANCE_ID 컬럼을 보시면 같은 ID (4) 를 가진 2개의 ROW가 보인다. 
- 그 중 첫번째 ROW는 STATUS 가 FAILED 이지만, 2번째 ROW는 COMPLETED이다


<img width="1000" alt="스크린샷 2022-05-19 오전 2 56 08" src="https://user-images.githubusercontent.com/54282927/169111038-9c28b087-a006-4f99-b89a-48f95e0e3afe.png">

<img width="1000" alt="스크린샷 2022-05-19 오전 2 58 54" src="https://user-images.githubusercontent.com/54282927/169111179-22e676d0-744c-4c6a-987d-955b805625b1.png">


<br>

Job Parameter requestDate=20220521로 생성된 BATCH_JOB_INSTACNE (id=4) 가 2번 실행되었고, 첫번째는 실패, 두번째는 성공했다는 것을 알 수 있다.
여기서 재밌는 것은 동일한 Job Parameter로 2번 실행했는데 같은 파라미터로 실행되었다는 에러가 발생하지 않았다는 점이다.
- Spring Batch는 동일한 Job Parameter로 성공한 기록("COMPLETED")이 있을때만 재수행이 안된다는 것을 알 수 있다.

<br>


## 3-3. JOB, JOB_INSTANCE, JOB_EXECUTION
지금까지 다룬 Spring Batch Job의 관계를 정리하면 아래와 같다.
<img width="500" alt="스크린샷 2022-05-19 오전 3 33 19" src="https://user-images.githubusercontent.com/54282927/169119065-5bc5a9d0-c5b9-41c3-b399-89bf90cc978a.png">


위 2개 테이블 이외에도 물론 Job 관련된 테이블은 더 있다.

### BATCH_JOB_EXECUTION_PARAM 
`BATCH_JOB_EXECUTION_PARAM` 테이블은 `BATCH_JOB_EXECUTION` 테이블이 생성될 당시에 입력 받은 Job Parameter를 담고 있다.
                                   
<img width="500" alt="스크린샷 2022-05-19 오전 3 36 05" src="https://user-images.githubusercontent.com/54282927/169119862-32e1072e-60db-4227-bf34-64a992e36aa8.png">


---
ref
- https://jojoldu.tistory.com/324
- https://jojoldu.tistory.com/325
- https://jojoldu.tistory.com/326
