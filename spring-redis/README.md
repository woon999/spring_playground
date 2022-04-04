# Spring Redis
spring 프로젝트에서 Redis 캐싱 데이터 set, get을 하는 방법은 두 가지가 있다.
1. RedisTemplate를 이용하는 방법 (low level API)
2. CrudRepository를 이용하는 방법 (high level API, JPA 처럼 사용)

더 추상화된 high level이 다루기 쉽다. 일반적인 용도는 2번 방법으로도 충분하므로 이번 실습에서는 `2번 방법`으로 진행 

<br> 

##1. Spring Redis 의존성 추가
spring boot 2.0이상부터는 jedis가 아닌 lettuce를 이용해서 redis에 접속하는게 디폴트이다. 
- gradle 의존성 > data-redis > lettuce 라이브러리

jedis, lettuce 모두 redis 접속 connecton pool 관리 라이브러리이며, lettuce가 성능이 더 좋기에 디폴트로 세팅되어 있다.
~~~
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
~~~

<br>

##2. redis 실행하기
docker로 redis 이미지를 받아와 실행
- redis 디폴트 폴트: 6379
- [docker hub _Redis](https://hub.docker.com/_/redis)
~~~
$ docker pull redis # redis 이미지 받기
$ docer images # redis 이미지 확인
$ docker run -p 6379:6379 --name some-redis -d redis # redis 시작하기
$ docker ps # redis 실행 확인 
~~~

<br>

##3. 캐싱할 데이터 만들기
### 3-1. 데이터 클래스 생성
- @RedisHash("{redis-key}") : Redis에 저장되는 Key
- @Id ~ : redis에 저장되는 ID
~~~
@RedisHash("Meetings")
public class Meeting {
	@Id
	private String id;
	private String title;
	private Date startAt;

	// getter, setter ...    
}
~~~

<br>

### 3-2. MeetingRepository 생성
~~~
public interface MeetingRepository extends CrudRepository<Meeting, String> {
}
~~~ 

### 3-3. 실행 테스트
앱이 실행되면 자동으로 run() 메서드도 실행된다.
~~~
@Component
public class DefaultPopulator implements ApplicationRunner {

	@Autowired
	private MeetingRepository meetingRepository;

	@Override public void run(ApplicationArguments args) throws Exception {
		for(int i=0; i<5; i++){
			Meeting meeting = new Meeting();
			meeting.setTitle("New Meeting" + i);
			meeting.setStartAt(new Date());
			meetingRepository.save(meeting);
		}

		meetingRepository.findAll().forEach(m -> {
			System.out.println("=============");
			System.out.println(m.getTitle() + " " + m.getStartAt());
		});
	}
}
~~~

<br>

### 실행 결과
~~~
=============
New Meeting3 Mon Apr 04 23:59:26 KST 2022
=============
New Meeting2 Mon Apr 04 23:59:26 KST 2022
=============
New Meeting0 Mon Apr 04 23:59:25 KST 2022
=============
New Meeting1 Mon Apr 04 23:59:26 KST 2022
=============
New Meeting4 Mon Apr 04 23:59:26 KST 2022
~~~

<br>

##4. Redis-cli 실행시켜서 확인하기
### 4-1. Redis-cli 실행
~~~
# 이전에 실행한 redis와 link해서 실행 
$ docker run -it --link some-redis:redis --rm redis redis-cli -h some-redis

# 또는
# 처음부터 network 생성해서 실행해도 됨 
$ docker network create redis-net
$ docker run -p 6379:6379 --network redis-net --name some-redis -d redis
$ docker run -it --network redis-net --rm redis redis-cli -h some-redis
~~~

### 4-2. Redis에 캐싱된 데이터 조회
#### (keys *) 캐싱된 모든 Key 조회
~~~
some-redis:6379> keys * 
# 결과
1) "Meetings:367ce70a-7534-41ed-b601-0587f170f6f0"
2) "Meetings:9d43eaf2-2b91-440b-94ec-9a97c90c3782"
3) "Meetings:0453bf09-b5d9-42f5-8c94-625b41c0100b"
4) "Meetings:abc86a73-a14b-43e5-9964-8eb49a5acd5f"
5) "Meetings:83c485eb-b41f-4c26-baab-b29b2cacaedd"
6) "Meetings"
~~~

#### (hgetall key:id) 캐싱된 values 조회하기
~~~
some-redis:6379> hgetall Meetings:367ce70a-7534-41ed-b601-0587f170f6f0
# 결과
1) "_class"
2) "com.example.springredis.Meeting"
3) "id"
4) "367ce70a-7534-41ed-b601-0587f170f6f0"
5) "startAt"
6) "1649085213278"
7) "title"
8) "New Meeting4"
~~~

#### (hget key:id value) key:id로 캐싱된 특정 value 조회하기
~~~
some-redis:6379> hget Meetings:367ce70a-7534-41ed-b601-0587f170f6f0 title # key:id로 캐싱된 value(title) 조회
# 결과
"New Meeting4"
~~~
