package com.example.springaop.aop;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Base64;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.example.springaop.dto.Member;

@Aspect
@Component
public class DecodeAop {

	// controller 패키지 하위에 모두 적용
	@Pointcut("execution(* com.example.springaop.controller..*.*(..))")
	private void cut(){}

	@Pointcut("@annotation(com.example.springaop.annotation.Decode)")
	private void enableDecode(){ }

	@Before("cut() && enableDecode()")
	public void before(JoinPoint joinPoint) throws UnsupportedEncodingException {
		System.out.println("----- before -----");

		Object[] args = joinPoint.getArgs();
		for(Object arg : args){
			if(arg instanceof Member){
				Member member = Member.class.cast(arg);
				String base64Email = member.getEmail();
				String email = new String(Base64.getDecoder().decode(base64Email.getBytes()), "UTF-8");
				member.setEmail(email);
				System.out.println("email = " + email);
			}
		}
		System.out.println("------------------");
	}

	@AfterReturning(value = "cut() && enableDecode()", returning = "returnObj")
	public void afterReturn(JoinPoint joinPoint, Object returnObj){
		if(returnObj instanceof Member){
			System.out.println("----- afterReturn -----");
			Member member = Member.class.cast(returnObj);
			String email = member.getEmail();
			String base64Email = Base64.getEncoder().encodeToString(email.getBytes());
			member.setEmail(base64Email);
			System.out.println("base64Email = " + base64Email);
		}
		System.out.println("------------------");
	}
}
