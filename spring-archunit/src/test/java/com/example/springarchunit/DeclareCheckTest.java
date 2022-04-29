package com.example.springarchunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.*;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

/**
 * 선언형 검사
 *
 * 선언형 검사 방식은 아키텍처에 맞도록 규칙을 먼저 선언해놓고 rule.check()를 사용해 클래스의 규칙을 한 번에 검사하는 방식이다.
 */
class DeclareCheckTest {

	@Test
	void packageDependencyTest(){
		JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.springarchunit.example");
		ArchRule rule = noClasses().that().resideInAPackage("..foo..")
			.should().dependOnClassesThat().resideInAPackage("..bar..");
		rule.check(importedClasses);
	}

	@Test
	void layeredArchitectureTest(){
		JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.springarchunit.layer");
		Architectures.LayeredArchitecture rule = layeredArchitecture()
			.layer("Controller").definedBy("..controller..")
			.layer("Service").definedBy("..service..")
			.layer("Repository").definedBy("..repository..")
			.whereLayer("Controller").mayNotBeAccessedByAnyLayer() // 다른 계층에 접근 불가
			.whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
			.whereLayer("Repository").mayOnlyBeAccessedByLayers("Controller");
		rule.check(importedClasses);
	}

}