# 9. ItemWriter
ItemWriter는 Spring Batch에서 사용하는 출력 기능이다.
Spring Batch가 처음 나왔을 때, ItemWriter는 ItemReader와 마찬가지로 item을 하나씩 다루었다.
그러나 Spring Batch2와 청크 (Chunk) 기반 처리의 도입으로 인해 ItemWriter에도 큰 변화가 있었다.  
**이 업데이트 이후 부터 ItemWriter는 item 하나를 작성하지 않고 Chunk 단위로 묶인 item List를 다룬다.** 

<br>

## 9-1. ItemWriter 주의사항
ItemWriter를 사용할 때 Processor에서 Writer에 List를 전달할 때 ItemWriter의 제네릭을 List로 선언해서는 문제를 해결할 수 없다.
- [write()메소드 오버라이딩 하여 Writer에 List 전달하기](https://jojoldu.tistory.com/140)


<br>

## 9-2. JpaItemWriter 사용하기 
### PayWriter 도메인 생성 
먼저 데이터를 읽은 다음 저장할 엔티티를 생성하자.

~~~
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class PayWriter {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	public PayWriter(Long amount, String txName, LocalDateTime txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}

	public PayWriter(Long id, Long amount, String txName, LocalDateTime txDateTime) {
		this.id = id;
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
	}
}
~~~

<br>

### JpaItemWriterJobConfiguration 클래스 생성 후 Job 생성하기 
`SELECT p FROM Pay p`으로 Pay에 저장된 데이터를 읽어들여서 `PayWriter`에 데이터를 저장하는 Job을 생성한다.
- Reader와 코드가 그렇게 다르지 않다. 
- 10번에서 다룰 ItemProcessor를 사용하여 읽어온 데이터를 PayWriter 도메인에 맞게 변경해주었다. (Stirng -> LocalDateTime)

~~~
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaItemWriterJob(){
		return jobBuilderFactory.get("jpaItemWriterJob")
			.start(jpaItemWriterStep())
			.build();
	}

	@Bean
	public Step jpaItemWriterStep(){
		return stepBuilderFactory.get("jpaItemWriterStep")
			.<Pay, PayWriter>chunk(CHUNK_SIZE)
			.reader(jpaItemWriterReader())
			.processor(jpaItemProcessor())
			.writer(jpaItemWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<Pay> jpaItemWriterReader() {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaItemWriterReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p")
			.build();
	}

	@Bean
	public ItemProcessor<Pay, PayWriter> jpaItemProcessor() {
		return pay -> new PayWriter(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public JpaItemWriter<PayWriter> jpaItemWriter() {
		JpaItemWriter<PayWriter> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}
}
~~~

<br>

### 실행 결과
Job을 실행하면 다음과 같이 정상적으로 PayWriter에 데이터가 입력된 것을 확인할 수 있다.  

<img width="700" alt="스크린샷 2022-05-20 오전 3 30 22" src="https://user-images.githubusercontent.com/54282927/169374182-89484a17-f1ca-4fcc-af94-6030cb703014.png">

<img width="1000" alt="스크린샷 2022-05-20 오전 3 29 51" src="https://user-images.githubusercontent.com/54282927/169374334-2da5f4c6-7c29-49e7-becf-b23748981400.png">
