# 스프링 배치 Spring Batch 사용하기
# 1. 기본 라이브러리 설정
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

# 2. Spring Batch Job 생성 후 실행하기
## 2-1. @EnableBatchProcessing 배치 기능 활성화하기
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

## 2-2. Simple Job 생성하기
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

##  2-3. DB 사용하기
### 2-3-1. 프로필 별로 application.yml 설정하기
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

### 2-3-2. Mysql schem-mysql.sql으로 테이블 생성하기
그냥 실행하면 에러가 발생한다. Spring Batch의 기본 도메인 테이블을 미리 생성해줘야 한다.  
- [shift + shift] -> `mysql-schema.sql` 검색 -> Mysql DB에 접속하여 해당 쿼리 실행
- 또는 yml에 아래와 같이 설정하면  `mysql-schema.sql`가 자동으로 실행되어 DB 테이블으 생성해준다.
~~~
spring:
  batch:
    jdbc:
      initialize-schema: always
~~~  

### Batch 테이블 생성 완료 
<img width="414" alt="스크린샷 2022-05-19 오전 1 00 45" src="https://user-images.githubusercontent.com/54282927/169092352-a9e080dd-2aee-4200-b9ed-60793ebfa091.png">


<br>

### Mysql 연동 성공 
그리고 앱을 실행하면 다음과 같이 정상적으로 Job이 실행되는 것을 확인할 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오전 1 18 00" src="https://user-images.githubusercontent.com/54282927/169092361-fff36f96-45a0-4e8b-9463-5e6cb132e47b.png">

<br>

# 3. Batch 메타 테이블
Spring Batch에 제공하는 메타 테이블들에 대해 알아보자.

<img width="700" alt="스크린샷 2022-05-19 오전 1 25 27" src="https://user-images.githubusercontent.com/54282927/169093663-c4a01171-6cba-427b-a3ed-c39645830146.png"> 

<br>

## 3-1. BATCH_JOB_INSTANCE 테이블
Job Instance 테이블은 Job Parameter에 따라 생성되는 테이블이다. Job Paramter란 Spring Batch가 실행될 때 외부에서 받을 수 있는 파라미터이다.
- 예를 들어, Job Parameter로 넘기면 Spring Batch에서는 해당 날짜 데이터로 조회/가공/입력 등의 작업을 할 수 있다.

<br>

해당 테이블을 보면 방금 실행했던 "simpleJob"을 확인할 수 있다. 
- `JOB_INSTANCE_ID`: BATCH_JOB_INSTANCE 테이블의 PK
- `JOB_NAME`: 수행한 Batch Job Name
- `JOB_KEY`: 동일한 Job이름의 JobInstance는 Job의 실행시점에 부여되는 고유한 JobParameter의 값을 통해 식별된다. 
             그리고 이렇게 식별되는 값의 직렬화(serialization)된 결과를 JOB_KEY라는 값으로 기록된다.

<img width="700" alt="스크린샷 2022-05-19 오전 1 28 38" src="https://user-images.githubusercontent.com/54282927/169094276-0b4c02f8-2c24-4eb2-8476-43ccfe1e3bbe.png">

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

## 3-2. BATCH_JOB_EXECUTION 테이블
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

## 3-4. BATCH_JOB_EXECUTION_PARAM
위 2개 테이블 이외에도 물론 Job 관련된 테이블은 더 있다.
`BATCH_JOB_EXECUTION_PARAM` 테이블은 `BATCH_JOB_EXECUTION` 테이블이 생성될 당시에 입력 받은 Job Parameter를 담고 있다.
                                   
<img width="500" alt="스크린샷 2022-05-19 오전 3 36 05" src="https://user-images.githubusercontent.com/54282927/169119862-32e1072e-60db-4227-bf34-64a992e36aa8.png">


<br>

# 4. Next 사용해보기 
Next는 이름 그대로 순차적으로 Step들을 연결시켜 실행해야할 때 사용한다.
- step1 -> (next)step2 -> (next)step3

<br>

## 4-1. StepNextJobConfiguration 클래스 생성
이번에는 새롭게 StepNextJobConfiguration을 생성해준다. 그리고 다음과 같이 Next를 사용하는 Job을 생성한다.

~~~
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StepNextJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job stepNextJob(){
		return jobBuilderFactory.get("stepNextJob") // "stepNextJob"이름을 가진 Batch Job 생성
			.start(step1())
			.next(step2())
			.next(step3())
			.build();
	}

	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1") // "step1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step1 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2") // "step2"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step2 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3") // "step3"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step3 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
~~~

<br>

## 4-2. version 설정 후 실행하기
이번에는 Job Parameter를 version=1로 변경하고나서 실행해보자. 

<img width="700" alt="스크린샷 2022-05-19 오후 1 26 11" src="https://user-images.githubusercontent.com/54282927/169205395-c63b2d64-6fce-4062-8e9c-fa0ea57dd810.png">

<br>

### 실행 결과
정상적으로 실행되었지만 기존에 실습했던 simpleJob도 같이 실행되었다.

<img width="1000" alt="스크린샷 2022-05-19 오후 1 26 49" src="https://user-images.githubusercontent.com/54282927/169205382-993bbef8-b512-4020-afb8-250d4c9a9fd8.png">


<br>

## 4-3. 지정한 Batch Job만 실행시키기
application.yml 설정을 사용하여 Batch Job을 지정하여 실행시킬 수 있다.


Spring Batch가 실행될때, Program arguments로 job.name 값이 넘어오면 해당 값과 일치하는 Job만 실행된다.
- ${job.name:NONE}는 job.name이 존재하면 해당 값을 넣어주고 job.name이 존재하지 않으면 NONE을 넣어주어 어떠한 Batch Job도 실행하지 않는다. 
~~~
spring:
    job:
      names: ${job.name:NONE}
~~~

<br>

그럼 이젠 다음과 같이 arguments값을 넣고 다시 실행해보자.
~~~
--job.name=stepNextJob version=2
~~~

<br>

### 실행 결과
stepNetxJob만 실행되는 것을 확인할 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오후 1 34 23" src="https://user-images.githubusercontent.com/54282927/169205841-b62989a7-fe3e-4eb3-aba1-81e32a2252d2.png">

실제 운영환경에서는 `java -jar batch-app.jar --job.name=stepNextJob`과 같이 실행한다.


<br>

# 5. 조건별 흐름 제어(Flow)
Next가 순차적으로 Step의 순서를 제어할 수 있다. 그런데 만약 실행 도중 오류가 발생한다면 나머지 뒤에 있는 step들은 실행되지 않는다는 것이다.
- 상황에 따라서 정상일 때는 [StepA -> StepB], 오류가 발생한 경우 [StepA -> StepC]로 조건 분기를 해야할 때가 있다.

<br>

이러한 상황을 위해서 Spring Batch Job에서는 조건별로 Step을 사용할 수 있다.

<br>

## 5-1. StepNextConditionalJobConfiguration 클래스 생성하기
시나리오는 다음과 같다.
- step1 실패 시나리오: step1 -> step3
- step1 성공 시나라오: step1 -> step2 -> step3

stepNextConditionJob()가 바로 Flow를 관리하는 코드이다.
- on(): 캐치할 ExitStatus 지정, "*"은 all과 같다.
- to(): 다음으로 이동한 Step 지정
- from(): 일종의 이벤트 리스너 역할. 
   - on(): 상태 값을 보고 일치하는 상태라면 to()에 포함된 step을 호출한다.
   - step1의 이벤트 캐치가 FAILED로 되어있는 상태에서 추가로 이벤트 캐치를 하려면 from을 써야만 한다.
- end(): FlowBuilder를 반환하는 end와 FlowBuilder를 종료하는 end 2개가 있다.
   - on("*") 다음에 오는 end: FlowBuilder를 반환하는 end
   - bulild() 앞에 있는 end: FlowBuilder를 종료하는 end
   - FlowBuilder를 반환하는 end 사용시 계속해서 from을 이어갈 수 있다.
~~~
/**
 * 조건 분기해서 사용해보기
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StepNextConditionalJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job stepNextConditionJob(){
		return jobBuilderFactory.get("stepNextConditionJob") // "stepNextConditionJob"이름을 가진 Batch Job 생성
			.start(conditionJobStep1())
				.on("FAILED") // ExitStatus가 FAILED일 경우
				.to(conditionJobStep3()) // step3로 이동
				.on("*") // step3 결과와 상관없이(모든 경우) 
				.end() // flow 종료
			.from(conditionJobStep1())
				.on("*")  // ExitStatus가 FAILED가 아닌 모든 경우 if-else의 else 구문
				.to(conditionJobStep2()) // step2로 이동
				.next(conditionJobStep3()) // step2가 정상 종료되면 step3로 이동
				.on("*") // 모든 경우
				.end() // flow 종료
			.end() // job 종료
			.build();
	}

	@Bean
	public Step conditionJobStep1(){
		return stepBuilderFactory.get("step1")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [1] --- stepNextConditionJob1 process");

				/**
				 * ExitStatus를 FAILED로 지정한다.
				 * 해당 status를 보고 flow가 진행된다.
				 */
				contribution.setExitStatus(ExitStatus.FAILED);

				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step conditionJobStep2(){
		return stepBuilderFactory.get("step2")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [2] --- stepNextConditionJob2 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step conditionJobStep3(){
		return stepBuilderFactory.get("step3")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [3] --- stepNextConditionJob3 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
~~~

<br>

### 실행 결과 (실패 시나리오)
job.name 값을 변경 후 실행해보자.
~~~
--job.name=stepNextConditionJob version=3
~~~

step1 -> step3의 Flow로 실행되는 것을 확인할 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오후 2 07 24" src="https://user-images.githubusercontent.com/54282927/169212961-f1f15781-c160-4f2c-9bb1-273b59fdd936.png">

<br>

### 실행 결과 (성공 시나리오)
ExitStatus를 FAILED로 설정해주는 로직을 주석처리하여 성공 시나리오로 실행해보자.
~~~
@Bean
public Step conditionJobStep1(){
    return stepBuilderFactory.get("step1")
        .tasklet(((contribution, chunkContext) -> {
            log.info(">>>>> [1] --- stepNextConditionJob1 process");

            /**
             * ExitStatus를 FAILED로 지정한다.
             * 해당 status를 보고 flow가 진행된다.
             */
            // contribution.setExitStatus(ExitStatus.FAILED);

            return RepeatStatus.FINISHED;
        }))
        .build();
}
~~~

step1 -> step2 -> step3의 Flow로 실행되는 것을 확인할 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오후 2 09 37" src="https://user-images.githubusercontent.com/54282927/169212986-20ace626-9140-4275-bdd7-67cb6801d429.png">

<br>

## Batch Status vs Exit Status 
BatchStatus: Job 또는 Step의 실행 상태를 나타낸다. 
   - BatchStatus로 사용 되는 값은 COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN이 있다.
   - 실행 중에는 BatchStatus.STARTED, 실패하면 BatchStatus.FAILED, 성공적으로 완료되면 BatchStatus.COMPLETED 이다.
   
ExitStatus: Step의 실행 결과 나타낸다. 
   - ExitStatus는 호출자에게 반환될 종료 코드가 포함되어 있기 때문에 가장 중요하다.

다음 예제는 'on'이 포함되어 있다.
~~~
from(stepA()).on("FAILED").to(stepB())
~~~

언뜻 보기에 on("FAILED")는 BatchStatus를 참조하는 것처럼 보일 수 있다. 
그러나 실제로는 실행 완료 후 상태를 나타내는 ExitStatus를 참조한다.
exitCode가 FAILED로 끝나게 되면 stepB()로 가라는 뜻이다.

- ref: https://docs.spring.io/spring-batch/docs/current/reference/html/index-single.html#batchStatusVsExitStatus

<br>

### exitCode 커스텀하기
다음 예제 시나리오가 있다.
- step1이 실패하며, Job 또한 실패하게 된다.
- step1이 성공적으로 수행되어 step2가 수행된다.
- step1이 성공적으로 완료되며, COMPLETED WITH SKIPS의 exit 코드로 종료 된다.
~~~
.start(step1())
    .on("FAILED")
    .end()
.from(step1())
    .on("COMPLETED WITH SKIPS")
    .to(errorPrint1())
    .end()
.from(step1())
    .on("*")
    .to(step2())
    .end()
~~~

<br>

위 코드에 나오는 "COMPLETED WITH SKIPS"는 ExitStatus에는 없는 코드이다.
원하는대로 처리되기 위해서는 COMPLETED WITH SKIPS exitCode를 반환하는 별도의 로직이 필요하다.

StepExecutionListener 에서는 먼저 Step이 성공적으로 수행되었는지 확인하고,
 StepExecution의 skip 횟수가 0보다 클 경우 COMPLETED WITH SKIPS 의 exitCode를 갖는 ExitStatus를 반환한다.
~~~
public class SkipCheckingListener extends StepExecutionListenerSupport {

    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) && 
              stepExecution.getSkipCount() > 0) {
            return new ExitStatus("COMPLETED WITH SKIPS");
        }
        else {
            return null;
        }
    }
}
~~~

<br> 

## 5-2. Decide로 Flow 조건 분기하기
위에서 진행했던 조건 분기 방식은 두 가지 문제점이 있다.
1. Step이 담당하는 역할이 2개 이상이 된다.
    - 실제 해당 Step이 처리해야 할 로직 외에도 분기 처리를 시키기 위해 ExitStatus 조작이 필요하다.
2. 다양한 분기 로직 처리의 어려움
    - ExitStatus를 커스텀하게 고치기 위해선 Listner를 생성하고 Job Flow에 등록하는 등 번거로움이 존재한다.
    
<br>

### JobExecutionDecider (DeciderJobConfiguration 클래스 생성하기)
이러한 문제점을 해결하기 위해 Spring Batch에서는 Step들의 Flow속에서 분기만 담당하는 타입이 있다.
JobExecutionDecider이다. 이를 다루기 위해 DeciderJobConfiguration 클래스를 생성해보자.
- Random 클래스를 사용하여 랜덤 값을 생성하여 홀짝을 구분하는 Decider를 생성한다.
- Decider 결과에 따라 홀이면 OddStep, 짝이면 evenStep으로 이동한다.
~~~
/**
 * Decider 사용해보기
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeciderJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
			.start(startStep())
			.next(decider()) // 홀짝을 구분하는 decider
			.from(decider()) //  만약 decider 상태 값이 홀수(ODD)라면 
			.on("ODD")   
			.to(oddStep()) // oddStep으로 이동 
			.from(decider()) // 만약 상태 값이 짝수(EVEN)라면
			.on("EVEN") 
			.to(evenStep()) // evenStep으로 이동 
			.end() 
			.build();
	}

	@Bean
	public Step startStep() {
		return stepBuilderFactory.get("startStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> step start");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> evenStep process");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> oddStep process");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	public static class OddDecider implements JobExecutionDecider {

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			Random rand = new Random();

			int randomNumber = rand.nextInt(50) + 1;
			log.info("randomNumber: {}", randomNumber);

			if(randomNumber % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
~~~

JobExecutionDecider 인터페이스를 구현한 OddDecider는 Step으로 처리하는 것이 아니기 때문에 `ExitStatus`가 아닌 `FlowExecutionStatus`으로 상태를 관리한다.

<br>

### 실행 결과
job.name을 deciderJob으로 변경한 후 실행해보자. 
~~~
--job.name=deciderJob version=6
~~~

여러번 실행해보면 다음과 같이 랜덤값에 따라 evenStep과 oddStep으로 분기되는 것을 확인할 수 있다.

<img width="1000" alt="스크린샷 2022-05-19 오후 2 57 15" src="https://user-images.githubusercontent.com/54282927/169220806-608c56a9-b3c7-4cad-aaf0-ec3ed2dc9e9a.png">

<img width="1000" alt="스크린샷 2022-05-19 오후 2 56 01" src="https://user-images.githubusercontent.com/54282927/169220495-8559ae68-365f-4d14-896d-8dee4e2d1eae.png">

<br>

# 6. Job Parameter와 Scope
## 6-1. Job Parameter
Springing Batch의 경우 외부 혹은 내부에서 파라미터를 받아 여러 Batch 컴포넌트에서 사용할 수 있게 지원하고 있다.
이를 Job Parameter라고 한다.

### 시스템 변수와 차이점
- Spring Batch는 같은 JobParameter로 같은 Job을 두 번 실행하지 않는다.
- 시스템 변수를 사용하면 Command line이 아닌 다른 방법으로 Job을 실행하기가 어렵다. 만약 실행해야 한다면 동적으로 값을 계속해서 변경시킬 수 있도록 구성해줘야 한다.
- Job Paramter에서 Late Binding을 지원(호환성 좋음)하는 데, 시스템 변수를 사용하면 해당 기능을 사용하지 못한다. 

### Late Binding
Late Binding은  개발자가 원하는 어느 타이밍이든 Job Parameter를 생성하고 Job을 수행할 수 있음을 알 수 있다.
~~~
// 해당 코드는 예제를 위한 코드로 실제로 Batch를 웹서버에서 관리하는 것은 권장되지 않는다.
RestController
@Slf4j
@RequiredArgsConstructor
public class JobLauncherController {
	private final JobLauncher jobLauncher;
	private final Job job;

	@GetMapping("/launchjob")
	public String handle(@RequestParam("fileName") String fileName) throws Exception {

		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addString("input.file.name", fileName)
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return "Done";
	}
}
~~~

<br>
 
예제를 보면 Controller에서 Request Parameter로 받은 값을 Job Parameter로 생성한다.
~~~
JobParameters jobParameters = new JobParametersBuilder()
                        .addString("input.file.name", fileName)
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
~~~

<br>

그리고 생성한 Job Parameter로 Job을 수행한다.
~~~
jobLauncher.run(job, jobParameters);
~~~

<br>

즉, 개발자가 원하는 어느 타이밍이든 Job Parameter를 생성하고 Job을 수행할 수 있음을 알 수 있다.
Job Parameter를 각각의 Batch 컴포넌트들이 사용하면 되니 변경이 심한 경우에도 쉽게 대응할 수 있다.


<br>

## 6-2. Scope (@StepScope, @JobScope)
Job Parameter를 사용하기 위해선 항상 Spring Batch 전용 Scope를 선언해야 한다. 
크게 `@StepScope`와 `@JobScope` 2가지가 있다. 사용법은 간단하다. SpEL로 선언해서 사용하면 된다.
- @StepScope는 Tasklet이나 ItemReader, ItemWriter, ItemProcessor에서 사용이 가능하다.
- @JobScope는 Step 선언문에서 사용이 가능하다.
~~~
  @Value("#{jobParameters[파라미터명]}")
~~~

<br>

현재 Job Parameter의 타입으로 사용할 수 있는 것은 Double, Long, Date, String 이 있다.
아쉽지만 LocalDate와 LocalDateTime이 없어 String 으로 받아 타입변환을 해서 사용해야 한다.

<br>

## 6-3. Scope Bean 생성 지점 
@StepScope는 Step 실행시점에 Bean이 생성되고 @JobScope는 Job 실행시점에 Bean이 생성된다.
이렇게 Bean의 생성시점을 어플리케이션 실행 시점이 아닌, Step 혹은 Job의 실행시점으로 지연시키면서 얻는 장점은 크게 2가지가 있다.
1. JobParamter **Late Binding** 가능. Job Parameter가 StepContext 또는 JobExecutionContext 레벨에서 할당시킬 수 있다.
    - 꼭 Application이 실행되는 시점이 아니더라도 **Controller나 Service와 같은 비즈니스 로직 처리 단계에서 Job Parameter를 할당시킬 수 있다.**
2. 동일한 컴포넌트를 병렬 혹은 동시에 사용할때 유용하다.
    - Step 안에 Tasklet이 있고, 이 Tasklet은 멤버 변수와 이 멤버 변수를 변경하는 로직이 있다고 가정해보자.
      -> 이 경우 @StepScope 없이 Step을 병렬로 실행시키게 되면 race condition이 발생한다. 
    - 하지만 @StepScope가 있다면 각각의 Step에서 별도의 Tasklet을 생성하고 관리하기 때문에 서로의 상태를 침범할 일이 없다.

<br>

## 6-4. Job Parameter 주의점
Job Parameter는 @Value를 통해 값을 받을 수 있다. 그렇다보니 주의해야 할 점이 있다.

### Job Parameter Scope Bean을 생성할때만 사용이 가능하다.
- Job Parameters는 Step이나, Tasklet, Reader 등 Batch 컴포넌트 Bean의 생성 시점에 호출할 수 있다.
- **정확히는 @StepScope, @JobScope Bean을 생성할때만 Job Parameters가 생성되기 때문에 사용할 수 있다.**
  
<br>

# 7. Chunk 지향 처리
## Chunk
chunk 사전적 용어는 "한 호흡에 말하는 길이"로, Spring Batch에서의 chunk란 데이터 덩어리로 작업 할 때 각 커밋 사이에 처리되는 row 수를 얘기한다. 

<br>

## Chunk 지향 처리
Chunk 지향 처리란 한 번에 하나씩 데이터를 읽어 hunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션을 다룬다.
물론 Chunk 단위로 트랜잭션을 수행하기 때문에 실패할 경우엔 해당 Chunk 만큼만 롤백이 되고, 이전에 커밋된 트랜잭션 범위까지는 반영이 된다.

다음 그림은 Chunk 단위로 실행하는 플로우를 보여준다.
- Reader와 Processor에서는 1건씩 다뤄지고, Writer에선 Chunk 단위로 처리된다.
  
<img width="610" alt="스크린샷 2022-05-19 오후 11 54 36" src="https://user-images.githubusercontent.com/54282927/169326605-d9c0c12b-0e63-4bef-b27e-76d665a421c2.png">

<br>

## 7-1. ChunkOrientedTasklet 엿보기 
Chunk지향 처리의 전체 로직을 다루는 것은 `ChunkOrientedTasklet` 클래스이다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 03 05" src="https://user-images.githubusercontent.com/54282927/169328401-01b7fa97-f8f6-455c-8a76-372cbfb4aa06.png">

<br>

### execute() 메서드
- `chunkProvider.provide()`로 Reader에서 Chunk size만큼 데이터를 가져온다
- `chunkProcessor.process()`에서 Reader로 받은 데이터를 가공(Processor)하고 저장(Writer)한다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 34 02" src="https://user-images.githubusercontent.com/54282927/169338064-faed7a41-fc12-4eab-85bd-f6630f721299.png">

<br>

### SimpleChunkProvider의 provide() 메서드 
데이터를 가져오는 chunkProvider의 가장 기본적인 구현체 SimpleChunkProvider의 provide()를 살펴보자.
- inputs이 ChunkSize만큼 쌓일때까지 read()를 호출한다.
- read() 내부에는  ItemReader.read를 호출한다.
- **즉, ItemReader.read에서 1건씩 데이터를 조회해 Chunk size만큼 데이터를 쌓는 것이 provide()가 하는 일이다**

<img width="700" alt="스크린샷 2022-05-20 오전 12 55 26" src="https://user-images.githubusercontent.com/54282927/169344506-27a6b41c-badf-4d37-97bf-22d5acf91acc.png">

<br>

### SimpleChunkProcessor의 process() 메서드  
Processor와 Writer 로직을 담고 있는 것은 ChunkProcessor가 담당한다. 
가장 기본적인 구현체 SimpleChunkProcessor의 process() 메서드르 살펴보자.  
- Chunk<I> inputs를 파라미터로 받는다.
    - 이 데이터는 앞서 chunkProvider.provide() 에서 받은 ChunkSize만큼 쌓인 item이다.
- transform() 에서는 전달 받은 inputs을 doProcess()로 전달하고 변환된 값을 받는다.
    - doProcess() 내부에는 ItemProcessor의 process()를 사용한다.
- transform()을 통해 가공된 대량의 데이터는 write()를 통해 일괄 저장된다.
    - write()는 저장이 될수도 있고, 외부 API로 전송할 수 도 있다.
    - 이는 개발자가 ItemWriter를 어떻게 구현했는지에 따라 달라진다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 59 14" src="https://user-images.githubusercontent.com/54282927/169345338-856bcc5f-6aff-4211-9a4a-60cbd3a11bad.png">

<br>


이렇게 가공된 데이터들은 SimpleChunkProcessor의 doWrite() 메서드를 호출하여 일괄 처리(배치 프로세싱)한다.
- writeItems(items) -> ItemWriter.write(items)

<br>

## 7-2. Page Size vs Chunk Size
PagingItemReader에서 사용하는 Page Size와 Chunk Size는 서로 의미하는 바가 다르다.
- Chunk Size는 한번에 처리될 트랜잭션 단위를 나타낸다. 
- Page Size는 한번에 조회할 Item의 Size를 나타낸다.

<br>

### 두 값이 다르다면?
PageSize가 10이고, ChunkSize가 50이라면 ItemReader에서 Page 조회가 5번 일어나면 1번의 트랜잭션이 발생하여 Chunk가 처리된다.
한번의 트랜잭션 처리를 위해 5번의 쿼리 조회가 발생하기 때문에 성능상 이슈가 발생할 수 있다. 그래서 Spring Batch의 PagingItemReader에는 클래스 상단에 다음과 같은 주석을 남겨두었다.

~~~
Setting a fairly large page size and using a commit interval that matches the page size should provide better performance.

Page size를 크게 설정하고 해당 사이즈와 일치하는 커밋 간격(Chunk size)를 사용하면 더 나은 성능을 제공한다.
~~~

<br>

성능상 이슈 외에도 2개 값을 다르게 할 경우 JPA를 사용한다면 영속성 컨텍스트가 깨지는 문제도 발생한다고 한다. [(참고)](https://jojoldu.tistory.com/146)

<br>  

따라서, 2개 값이 의미하는 바가 다르지만 여러 이슈로 2개 값을 동일하게 설정하는 것이 좋다.
  
<br>

# 8.ItemReader
Spring Batch의 Chunk Tasklet은 다음과 같이 진행된다.

<img width="619" alt="스크린샷 2022-05-20 오전 1 27 16" src="https://user-images.githubusercontent.com/54282927/169350801-dc40cfc6-6b22-48c8-9785-321009a3a449.png">

ItemReader는 말 그대로 데이터를 읽어들인다. DB 데이터뿐만 아니라 File, XML, JSON, CSV 등 다른 데이터 소스를 배치 처리의 입력으로 사용할 수 있다.
또한 JMS와 같은 다른 유형의 데이터 소스도 지원한다. 정리하면 다음과 같다. 
- 입력 데이터에서 읽어오기
- 파일에서 읽어오기
- Database에서 읽어오기
- Java Message Service등 다른 소스에서 읽어오기
- 본인만의 커스텀한 Reader로 읽어오기

<br>

## 8-1.ItemStream
ItemReader와 ItemStream 인터페이스를 직접 구현해서 원하는 형태의 ItemReader를 만들 수 있다.
다만 Spring Batch에서 대부분의 데이터 형태는 ItemReader로 이미 제공하고 있기 때문에 커스텀한 ItemReader를 구현할 일은 많이 없다.
- open(), close()는 스트림을 열고 닫는다.
- update()를 사용하면 Batch 처리의 상태를 업데이트 할 수 있다.

<br>

## 8-2. ItemReader 주의사항
JpaRepository를 ListItemReader, QueueItemReader에 사용하면 안된다.
- 간혹 JPA의 조회 쿼리를 쉽게 구현하기 위해 JpaRepository를 이용해서 new ListItemReader<>(jpaRepository.findByAge(age))로 Reader를 구현하는 코드가 종종 있다.
- 이렇게 할 경우 Spring Batch의 장점인 페이징 & Cursor 구현이 없어 대규모 데이터 처리가 불가능하다. (물론 Chunk 단위 트랜잭션은 됩니다.)
- 만약 정말 JpaRepository를 써야 하신다면 RepositoryItemReader를 사용하시는 것을 추천한다.
    - Paging을 기본적으로 지원한다.
Hibernate, JPA 등 영속성 컨텍스트가 필요한 Reader 사용시 fetchSize와 ChunkSize는 같은 값을 유지해야 한다.
   
   
<br>

## 8-3. JpaPagingItemReader 사용해보기
### Pay 도메인 생성 
먼저 DB에서 읽어올 데이터를 만들기 위해 pay 도메인부터 생성해준다.
~~~
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	public Pay(Long amount, String txName, String txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
	}
	
	public Pay(Long id, Long amount, String txName, String txDateTime) {
		this.id = id;
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
	}
}
~~~

<br>

그런 다음 sql문으로 db 테이블 생성 후 데이터를 넣어준다.
~~~
create table pay (
  id         bigint not null auto_increment,
  amount     bigint,
  tx_name     varchar(255),
  tx_date_time datetime,
  primary key (id)
) engine = InnoDB;

insert into pay (amount, tx_name, tx_date_time) VALUES (1000, 'trade1', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (2000, 'trade2', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (3000, 'trade3', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (4000, 'trade4', '2018-09-10 00:00:00');
~~~

<br>

### JpaPagingItemReaderJobConfiguration 클래스 생성 후 Job 설정하기
`SELECT p FROM Pay p WHERE amount >= 2000`문으로 조건을 주어 결제 금액이 2,000원 이상인 데이터만 조회하는 Job을 생성해준다.
- Chunk size는 10으로, Page size도 이와 같게 설정해준다.
~~~
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaPagingItemReaderJob() {
		return jobBuilderFactory.get("jpaPagingItemReaderJob")
			.start(jpaPagingItemReaderStep())
			.build();
	}

	@Bean
	public Step jpaPagingItemReaderStep() {
		return stepBuilderFactory.get("jpaPagingItemReaderStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jpaPagingItemReader())
			.writer(jpaPagingItemWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<Pay> jpaPagingItemReader() {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaPagingItemReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p WHERE amount >= 2000")
			.build();
	}

	private ItemWriter<Pay> jpaPagingItemWriter() {
		return list -> {
			for (Pay pay : list) {
				log.info(">>>>> current pay: {}", pay);
			}
		};

	}
}
~~~

<br>

### 실행 결과
Job을 실행시키면 다음과 같이 2,000원 이상인 결제 내역만 정상적으로 읽어오는 것을 확인할 수 있다.
<img width="800" alt="스크린샷 2022-05-20 오전 3 01 37" src="https://user-images.githubusercontent.com/54282927/169369804-72d97f75-bb86-44d9-92e6-bc52a09b4103.png">

<br> 

# 9. ItemWriter
ItemWriter는 Spring Batch에서 사용하는 출력 기능이다.
Spring Batch가 처음 나왔을 때, ItemWriter는 ItemReader와 마찬가지로 item을 하나씩 다루었다.
그러나 Spring Batch2와 청크 (Chunk) 기반 처리의 도입으로 인해 ItemWriter에도 큰 변화가 있었다.  
**이 업데이트 이후 부터 ItemWriter는 item 하나를 작성하지 않고 Chunk 단위로 묶인 item List를 다룬다.** 

<br>

## ItemWriter 주의사항
ItemWriter를 사용할 때 Processor에서 Writer에 List를 전달할 때 ItemWriter의 제네릭을 List로 선언해서는 문제를 해결할 수 없다.
- [write()메소드 오버라이딩 하여 Writer에 List 전달하기](https://jojoldu.tistory.com/140)

<br>

# 10. ItemProcessor
ItemProcessor는 데이터를 가공 (혹은 처리)한다. 해당 기능은 필수가 아니다.
- ItemProcessor는 데이터를 가공하거나 필터링하는 역할을 한다. 이는 Writer 부분에서도 충분히 구현 가능하다.
- 그럼에도 ItemProcessor를 쓰는 것은 Reader, Writer와는 별도의 단계로 기능이 분리되기 때문이다.

<br>

ChunkSize 단위로 묶은 데이터를 한번에 처리하는 ItemWriter와는 달리 ItemProcessor는 Reader에서 넘겨준 데이터 개별 건을 가공 및 처리한다.
일반적은 ItemProcessor를 사용하는 방법은 2가지이다.
- 변환: Reader에서 읽은 데이터를 원하는 타입으로 변환해서 Writer에 넘긴다.
- 필터: Reader에서 넘겨준 데이터를 Writer로 넘겨줄 것인지를 결정할 수 있다. null을 반환하면 Writer에 전달되지 않는다.


<br>

## 기본 사용법
- I: ItemReader에서 받을 데이터 타입
- O: ItemWriter에 보낼 데이터 타입
~~~
public interface ItemProcessor<I, O> {
  O process(I item) throws Exception;
}
~~~ 





---
ref
- [기억보단 기록을 블로그 - Spring Batch 가이드 시리즈](https://jojoldu.tistory.com/tag/Spring%20Batch?page=6)
