# 8.ItemReader
Spring Batch의 Chunk Tasklet은 다음과 같이 진행된다.

<img width="619" alt="스크린샷 2022-05-20 오전 1 27 16" src="https://user-images.githubusercontent.com/54282927/169350801-dc40cfc6-6b22-48c8-9785-321009a3a449.png">

ItemReader는 말 그대로 데이터를 읽어들인다. DB 데이터뿐만 아니라 File, XML, JSON, CSV 등 다른 데이터 소스를 배치 처리의 입력으로 사용할 수 있다.
또한 JMS와 같은 다른 유형의 데이터 소스도 지원한다. 정리하면 다음과 같다. 
- 입력 데이터에서 읽어오기
- 파일에서 읽어오기
- Database에서 읽어오기
- Java Message Service등 다른 소스에서 읽어오기
- 본인만의 커스텀한 Reader로 읽어오기

<br>

## 8-1.ItemStream
ItemReader와 ItemStream 인터페이스를 직접 구현해서 원하는 형태의 ItemReader를 만들 수 있다.
다만 Spring Batch에서 대부분의 데이터 형태는 ItemReader로 이미 제공하고 있기 때문에 커스텀한 ItemReader를 구현할 일은 많이 없다.
- open(), close()는 스트림을 열고 닫는다.
- update()를 사용하면 Batch 처리의 상태를 업데이트 할 수 있다.

<br>

## 8-2. ItemReader 주의사항
JpaRepository를 ListItemReader, QueueItemReader에 사용하면 안된다.
- 간혹 JPA의 조회 쿼리를 쉽게 구현하기 위해 JpaRepository를 이용해서 new ListItemReader<>(jpaRepository.findByAge(age))로 Reader를 구현하는 코드가 종종 있다.
- 이렇게 할 경우 Spring Batch의 장점인 페이징 & Cursor 구현이 없어 대규모 데이터 처리가 불가능하다. (물론 Chunk 단위 트랜잭션은 됩니다.)
- 만약 정말 JpaRepository를 써야 하신다면 RepositoryItemReader를 사용하시는 것을 추천한다.
    - Paging을 기본적으로 지원한다.
Hibernate, JPA 등 영속성 컨텍스트가 필요한 Reader 사용시 fetchSize와 ChunkSize는 같은 값을 유지해야 한다.
   
   
<br>

## 8-3. JpaPagingItemReader 사용해보기

### Pay 도메인 생성 
먼저 DB에서 읽어올 데이터를 만들기 위해 pay 도메인부터 생성해준다.

~~~
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	public Pay(Long amount, String txName, String txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
	}
	
	public Pay(Long id, Long amount, String txName, String txDateTime) {
		this.id = id;
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
	}
}
~~~

<br>

그런 다음 sql문으로 db 테이블 생성 후 데이터를 넣어준다.
~~~
create table pay (
  id         bigint not null auto_increment,
  amount     bigint,
  tx_name     varchar(255),
  tx_date_time datetime,
  primary key (id)
) engine = InnoDB;

insert into pay (amount, tx_name, tx_date_time) VALUES (1000, 'trade1', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (2000, 'trade2', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (3000, 'trade3', '2018-09-10 00:00:00');
insert into pay (amount, tx_name, tx_date_time) VALUES (4000, 'trade4', '2018-09-10 00:00:00');
~~~

<br>

### JpaPagingItemReaderJobConfiguration 클래스 생성 후 Job 설정하기
`SELECT p FROM Pay p WHERE amount >= 2000`문으로 조건을 주어 결제 금액이 2,000원 이상인 데이터만 조회하는 Job을 생성해준다.
- Chunk size는 10으로, Page size도 이와 같게 설정해준다.

~~~
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaPagingItemReaderJob() {
		return jobBuilderFactory.get("jpaPagingItemReaderJob")
			.start(jpaPagingItemReaderStep())
			.build();
	}

	@Bean
	public Step jpaPagingItemReaderStep() {
		return stepBuilderFactory.get("jpaPagingItemReaderStep")
			.<Pay, Pay>chunk(CHUNK_SIZE)
			.reader(jpaPagingItemReader())
			.writer(jpaPagingItemWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<Pay> jpaPagingItemReader() {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaPagingItemReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p WHERE amount >= 2000")
			.build();
	}

	private ItemWriter<Pay> jpaPagingItemWriter() {
		return list -> {
			for (Pay pay : list) {
				log.info(">>>>> current pay: {}", pay);
			}
		};

	}
}
~~~

<br>

### 실행 결과
Job을 실행시키면 다음과 같이 2,000원 이상인 결제 내역만 정상적으로 읽어오는 것을 확인할 수 있다.

<img width="800" alt="스크린샷 2022-05-20 오전 3 01 37" src="https://user-images.githubusercontent.com/54282927/169369804-72d97f75-bb86-44d9-92e6-bc52a09b4103.png">
