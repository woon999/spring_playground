# ArchUnit로 아키텍처 검사하기 
`아키텍처(architecture)`는 정의에 따르면 컴퓨터 공학에서 컴퓨터 시스템의 기능, 조직, 구현에 대한 법칙과 방법을 의미한다.
- https://www.archunit.org/

<br>

ArchUnit은 plain Java unit test 프레임워크를 사용하여 Java 코드의 아키텍처를 검사하기 위한 심플하면서 확장 가능한 오픈소스이다.
즉, ArchUnit은 패키지와 클래스, 레이어와 슬라이스 간의 종속성을 확인하고 순환 참조를 체크할 수 있다.
주어진 Java 바이트코드를 분석하여 모든 클래스를 Java 코드 구조로 가져옴으로써 이를 수행한다.
- 패키지와 클래스 의존관계 확인
- 상속 관계, 순환 참조 검사
- 레이어 아키텍처 검사
- 직접 규칙을 정의해 코딩 컨벤션 준수 여부 검사

<br>

## ArchUnit 사용법
### 의존성 검사 예제
rule: foo 패키지에서 bar 패키지에 의존할 수 없도록 규칙 정의한다.

<img width="352" alt="스크린샷 2022-05-05 오후 9 42 30" src="https://user-images.githubusercontent.com/54282927/166925224-d13e3b17-6536-4d48-8935-afca62fce7ba.png">

<br>

### 방법 1. 선언형 검사
아키텍처에 맞도록 규칙을 먼저 선언해놓고, rule.check()을 사용해 모든 클래스의 규칙을 한 번에 검사하는 방식이다. 
ArchUnit으로 다음과 같이 테스트 코드를 작성한다.
~~~
@Test
void packageDependencyTest(){
    JavaClasses importedClasses = new ClassFileImporter().importPackages("com.example.springarchunit.example");
    ArchRule rule = noClasses().that().resideInAPackage("..foo..")
        .should().dependOnClassesThat().resideInAPackage("..bar..");
    rule.check(importedClasses);
}
~~~

<br> 

#### 레이어 아키텍처 
예를 들어, Service -> Controller, Reposiotry -> Controller라는 레이어간의 규칙을 정의했다고 하면 다음과 같이 검사할 수 있다. 
~~~
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
~~~

<br>

### 방법 2. ArchUnit 테스트 클래스 생성 
아키텍처 검사를 하는 클래스를 따로 생성하여 관리할 수도 있다.
- 다음과 같이 클래스 위에 `@AnalyzeClasses(packagesOf = App.class)`을 명시한다. App.class는 스프링 앱을 실행하는 클래스를 입력해주면 된다.
- @ArchTest를 명시하고 똑같이 아키텍처 검사 코드를 작성하면 rule.check()까지 자동으로 검사해준다.
~~~
@AnalyzeClasses(packagesOf = App.class)
public class LayeredArchitectureTest {
	@ArchTest
	LayeredArchitecture layeredArchitectureRule = layeredArchitecture()
		.layer("Controller").definedBy("..controller..")
		.layer("Service").definedBy("..service..")
		.layer("Repository").definedBy("..repository..")
		.whereLayer("Controller").mayNotBeAccessedByAnyLayer() // 다른 계층에 접근 불가
		.whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
		.whereLayer("Repository").mayOnlyBeAccessedByLayers("Controller");
}

~~~

---
ref
- https://www.inflearn.com/course/the-java-application-test/dashboard
- https://d2.naver.com/helloworld/9222129
