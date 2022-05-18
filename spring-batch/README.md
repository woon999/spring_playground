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