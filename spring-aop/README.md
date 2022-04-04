# Spring AOP


## 의존성 추가
~~~
implementation 'org.springframework.boot:spring-boot-starter-aop'
~~~


## 1. 메서드 파라미터, 리턴값 로 AOP
로그를 찍어 어떤 Http Method가 실행되고 어떤 값이 리턴되는지 확인할 수 있어서 디버깅할 때 아주 유용하다. 
- @Aspect: AOP 클래스에 선언
- @Pointcut
- @Before: cut이 적용된 메서드 시작전 실행 
- @AfterReturning: cut 메서드가 값을 리턴한 후 실행
~~~
@Aspect
@Component
public class ParameterAop {

	// controller 패키지 하위에 모두 적용
	@Pointcut("execution(* com.example.springaop.controller..*.*(..))")
	private void cut(){}

	@Before("cut()")
	public void before(JoinPoint joinPoint){
		System.out.println("----- before -----");

		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		System.out.println("method = " + method.getName());
		Object[] args = joinPoint.getArgs(); // 메서드의 args 가져오기
		for (Object arg : args) {
			System.out.println("type = " + arg.getClass().getSimpleName());
			System.out.println("value = " + arg);
		}
		System.out.println("------------------");

	}

	@AfterReturning(value = "cut()", returning = "returnObj")
	public void afterReturn(JoinPoint joinPoint, Object returnObj){
		System.out.println("----- afterReturn -----");
		System.out.println("returnObj = " + returnObj);
		System.out.println("------------------");
	}

}
~~~

<br>

## 2. 메서드 실행 시간 측정 AOP 
### 2-1. Timer 애노테이션 생성
~~~
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Timer {
}
~~~

<br>

### 2-2. 실행 시간 측정할 API 예제
실행 시간 측정할 API에 @Timer 애노테이션을 명시해줌.
- 예시로 Thread.sleep 2초 설정  
~~~
@Timer
@DeleteMapping("/delete")
public String delete() throws InterruptedException {
    Thread.sleep(1000*2);
    return "delete";
}
~~~

<br>

### 2-3. TimerAop 클래스 작성
- cut(): controller 하위 모든 패키지에 적용
- enableTimer(): @Timer 애노테이션이 명시된 메서드에 적용
- @Around: 타겟 메서드를 감싸서 특정 Advice를 실행한다는 의미이다 
- proceed(): Proceed with the next advice or target method invocation 타겟 메서드 실행해서 리턴값 받아옴 
~~~
@Aspect
@Component
public class TimerAop {

	// controller 패키지 하위에 모두 적용
	@Pointcut("execution(* com.example.springaop.controller..*.*(..))")
	private void cut(){}

	@Pointcut("@annotation(com.example.springaop.annotation.Timer)")
	private void enableTimer(){}

	@Around("cut() && enableTimer()")
	public void around(ProceedingJoinPoint joinPoint) throws Throwable{
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object result = joinPoint.proceed(); // proceed() 메서드가 실행됨. 메서드 리턴이 있으면 result에 담음

		stopWatch.stop();

		System.out.println("total time = " + stopWatch.getTotalTimeSeconds());
	}
}
~~~