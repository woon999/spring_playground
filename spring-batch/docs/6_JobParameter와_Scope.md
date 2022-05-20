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
  