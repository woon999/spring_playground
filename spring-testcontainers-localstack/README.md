# Spring Testcontainers LocalStack
## 의존성 추가
- [localstack](https://localstack.cloud/)
- [testcontainers](https://www.testcontainers.org/modules/localstack)
- [aws-cloud-aws-starter](https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-aws)
~~~
// localstack
testImplementation "org.testcontainers:localstack:1.16.3"
// testContainers
testImplementation "org.testcontainers:junit-jupiter:1.16.3"
// aws s3
implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE'
~~~

<br>

## yml 설정
### application.yml
aws-cloud-aws-starter을 사용하면 metadata로 region을 설정해줘야 한다.
~~~
cloud:
  aws:
    region:
      static: ap-northeast-2
    stack:
      auto: false
~~~

### application-test.yml
그리고 테스트에서 생성한 빈을 오버라이딩할 것이므로 test.yml에 빈 오버라이딩 설정을 true해준다.
~~~
spring:
  main:
    allow-bean-definition-overriding: true
~~~

<br>

## Localstack Containers 사용해보기
~~~
@Testcontainers
public class LocalStackTestContainersTest {
	private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

	@Container
	LocalStackContainer localStackContainer = new LocalStackContainer(LOCALSTACK_IMAGE)
		.withServices(S3);

	@Test
	void test(){
		AmazonS3 amazonS3 = AmazonS3ClientBuilder
			.standard()
			.withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
			.withCredentials(localStackContainer.getDefaultCredentialsProvider())
			.build();

		String bucketName = "foo";
		amazonS3.createBucket(bucketName);
		System.out.println(bucketName +" 버킷 생성");

		String key = "foo-key";
		String content = "foo-content";
		amazonS3.putObject(bucketName, key, content);
		System.out.println("파일을 업로드하였습니다. key=" + key +", content=" + content);

		S3Object object = amazonS3.getObject(bucketName, key);
		System.out.println("파일을 가져왔습니다. = " + object.getKey());
	}
}
~~~


<br>

## Integration Test에서 LocalStack
### 1. 빈 오버라이딩하기
#### Local S3Config 클래스
- AmazonS3 빈 생성 
~~~
@Configuration
public class S3Config {
	@Bean
	public AmazonS3 amazonS3() {
		AmazonS3 s3Builder = AmazonS3ClientBuilder.standard()
			.build();
		return s3Builder;
	}
}
~~~

<br>

#### Test S3Config 클래스
test용 AmazonS3 빈 생성 
~~~
@TestConfiguration
public class LocalStackS3Config {
	private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack");

	// GenericContainer start(), stop() 메서드로 생명주기 설정
	@Bean(initMethod = "start", destroyMethod = "stop")
	public LocalStackContainer localStackContainer(){
		return new LocalStackContainer(LOCALSTACK_IMAGE)
			.withServices(S3);
	}

	@Bean
	public AmazonS3 amazonS3(LocalStackContainer localStackContainer){
		return  AmazonS3ClientBuilder
			.standard()
			.withEndpointConfiguration(localStackContainer.getEndpointConfiguration(S3))
			.withCredentials(localStackContainer.getDefaultCredentialsProvider())
			.build();
	}
}
~~~


<br>

### Integration Test
~~~
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LocalStackS3Config.class)
class S3ControllerTest {
	@Autowired
	AmazonS3 amazonS3;
	@Autowired
	TestRestTemplate testRestTemplate;

	@BeforeEach
	void setUp(){
		amazonS3.createBucket(S3Controller.BUCKET_NAME);
	}

	@Test
	public void test(){
		String id = "1";
		String content = "test content";

		String uploadResponse = testRestTemplate.postForObject("/s3/{id}", content, String.class, id);
		System.out.println("upload response = " + uploadResponse);

		String readResponse = testRestTemplate.getForObject("/s3/{id}", String.class, id);
		System.out.println("read response = " + readResponse);
	}
}
~~~