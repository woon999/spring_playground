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

