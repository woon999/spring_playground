# Spring Redis Cache 

redis yml 설정
~~~
spring:
  cache:
    type: redis
    redis:
      time-to-live: 60000 #ms
      cache-null-values: true
~~~

<br>

## 캐시 객체 직렬화 Serializable
~~~
public class Product implements Serializable {
	//...
}
~~~

<br> 

## API에 @Cache~ 애노테이션 추가

### @Cacheable(key = "#id", value = "product")
Cache Miss인 경우 DB 데이터 조회해서 등록, Cache Hit인 경우 바로 캐시에서 조회 
- cacheNames(=value): 캐시 이름을 설정하는 속성
- key: 캐시 key 정하는 속성
- condition: 조건 설정 
 
 <br>
   
### @CachePut(key = "#id", value = "product")
- 캐시 정보 갱신. Cache Miss인 경우 캐시 생성 

<br>

### @CacheEvict(key = "#id", value = "product")
캐시 초기화
- cacheNames(value): 캐시 이름을 설정하는 속성
- key: 캐시 key 정하는 속성
- allEntries: true - 전체 캐시 삭제



<br>

## @EnableCaching 애노테이션 추가
~~~
@SpringBootApplication
@EnableCaching
public class SpringRedisCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRedisCacheApplication.class, args);
	}

}
~~~