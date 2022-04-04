# Spring AOP


## 의존성 추가
~~~
implementation 'org.springframework.boot:spring-boot-starter-aop'
~~~


## 1. 메서드 파라미터, 리턴값 로그 AOP
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
	private void cut(){

	}

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
