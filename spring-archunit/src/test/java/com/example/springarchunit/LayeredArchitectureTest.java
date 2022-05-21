package com.example.springarchunit;

import static com.tngtech.archunit.library.Architectures.*;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures.*;

@AnalyzeClasses(packagesOf = App.class)
public class LayeredArchitectureTest {

	/**
	 * 레이어 아키텍처 검사
	 * 패키지명으로 레이어를 간단하게 정의하고, 각 레이어에서 어떠한 방향으로 접근할 수 있고 접근할 수 없는지를 규칙으로 정의
	 * Controller
	 * Controller -> Service, Repository
	 * Service -> Repository
	 *
	 * ref: https://www.archunit.org/userguide/html/000_Index.html#_layer_checks
	 */
	@ArchTest
	LayeredArchitecture layeredArchitectureRule = layeredArchitecture()
		.layer("Controller").definedBy("..controller..")
		.layer("Service").definedBy("..service..")
		.layer("Repository").definedBy("..repository..")
		.whereLayer("Controller").mayNotBeAccessedByAnyLayer() // 다른 계층에 접근 불가
		.whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
		.whereLayer("Repository").mayOnlyBeAccessedByLayers("Controller", "Service");


}
