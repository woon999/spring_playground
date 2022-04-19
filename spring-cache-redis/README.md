# Spring Redis Cache 
### 멤버(Member)가 장바구니(Cart)에 아이템(Item) 담기
- Member 캐시 기능은 CrudRepository로 구현
- Cart 캐시 기능은 RedisTemplate으로 구현 

자세한 내용은 블로그 설명 참고: https://loosie.tistory.com/807

<br> 

## 1. Spring Redis 의존성 추가
spring boot 2.0이상부터는 jedis가 아닌 lettuce를 이용해서 redis에 접속하는게 디폴트이다. 
- gradle 의존성 > data-redis > lettuce 라이브러리

jedis, lettuce 모두 redis 접속 connecton pool 관리 라이브러리이며, lettuce가 성능이 더 좋기에 디폴트로 세팅되어 있다.
~~~
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
~~~

<br>

## 2. redis 실행하기
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

## 3.Redis Config
#### Serializer
- JdkSerializationRedisSerializer: 디폴트로 등록되어있는 Serializer이다.
- StringRedisSerializer: String 값을 정상적으로 읽어서 저장한다. 그러나 엔티티나 VO같은 타입은 cast 할 수 없다.
- Jackson2JsonRedisSerializer(classType.class): classType 값을 json 형태로 저장한다. 특정 클래스(classType)에게만 직속되어있다는 단점이 있다.
- GenericJackson2JsonRedisSerializer: 모든 classType을 json 형태로 저장할 수 있는 범용적인 Jackson2JsonRedisSerializer이다. 캐싱에 클래스 타입도 저장된다는 단점이 있지만 RedisTemplate을 이용해 다양한 타입 객체를 캐싱할 때 사용하기에 좋다

~~~
@Configuration
@EnableCaching
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		// Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(ItemDto.class);
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		// redisTemplate.setValueSerializer(new StringRedisSerializer());
		// redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
		return redisTemplate;
	}
}
~~~

<br>

##4. Redis-cli 실행시켜서 확인하기
~~~
# 이전에 실행한 redis와 link해서 실행 
$ docker run -it --link some-redis:redis --rm redis redis-cli -h some-redis

# 또는
# 처음부터 network 생성해서 실행해도 됨 
$ docker network create redis-net
$ docker run -p 6379:6379 --network redis-net --name some-redis -d redis
$ docker run -it --network redis-net --rm redis redis-cli -h some-redis
~~~




