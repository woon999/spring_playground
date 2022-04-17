# Spring Redis Cache 
### 멤버(Member)가 장바구니(Cart)에 아이템(Item) 담기
- Member 캐시 기능은 CrudRepository로 구현
- Cart 캐시 기능은 RedisTemplate으로 구현 

자세한 내용은 블로그 설명 참고: https://loosie.tistory.com/807

<br> 

### Redis Config
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




