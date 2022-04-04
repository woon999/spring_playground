package com.example.springaop.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {

	// controller 패키지 하위에 모두 적용
	@Pointcut("execution(* com.example.springaop.controller..*.*(..))")
	private void cut(){}

	@Pointcut("@annotation(com.example.springaop.annotation.Timer)")
	private void enableTimer(){ }

	@Around("cut() && enableTimer()")
	public void around(ProceedingJoinPoint joinPoint) throws Throwable{
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object result = joinPoint.proceed(); // proceed() 메서드가 실행됨. 메서드 리턴이 있으면 result에 담음
		System.out.println("result = " + result);
		stopWatch.stop();

		System.out.println("total time = " + stopWatch.getTotalTimeSeconds());
	}

}
