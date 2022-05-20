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
