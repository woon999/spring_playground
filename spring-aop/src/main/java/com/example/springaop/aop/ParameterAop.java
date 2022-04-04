package com.example.springaop.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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
