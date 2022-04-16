# Spring Local-Memory로 캐싱하기

## @EnableCaching
spring-context에서 기본으로 제공하므로 Redis나 다른 캐시 저장소를 사용할 것이 아니라면 특정한 설정은 필요없다. Spring 앱 위에 @EnableCaching을 명시해주면 캐싱 기능 사용이 가능하다.
~~~
@SpringBootApplication
@EnableCaching
public class SpringCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCacheApplication.class, args);
	}
}
~~~

<br> 

## @Cacheable
캐시할 메서드 위에 선언한다. key는 SpEL을 사용해서 설정하고 싶은 속성만 선택할 수 있다.
- (Cache Hit) 만약 캐시에 해당 정보가 있으면 캐시에서 해당 정보를 꺼내서 반환한다.
- (Cache Miss) 없으면 DB에서 읽어온 데이터를 캐시에 저장한다.
~~~
@GetMapping("/{id}")
@Cacheable(key = "#id", cacheNames = "Member")
public Member findOne(@PathVariable Long id) {
    Member member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 멤버는 존재하지 않습니다."));
    log.info("Member fetching from DB :" + id);
    return member;
}
~~~

<br>

## @CacheEvict
캐시를 삭제할 메서드 위에 선언한다.
- DB와 Cache에 저장된 데이터를 동시에 삭제한다.
~~~
@DeleteMapping("/{id}")
@CacheEvict(key = "#id", cacheNames = "Member")
public String deleteOne(@PathVariable Long id) {
    memberRepository.deleteById(id);
    log.info("Member delete from Cache :" + id);
    return "delete";
}
~~~

<br>

## @CachePut
메서드 실행을 방해하지 않고 캐시 내용을 업데이트 할 수 있다. 즉, 메서드가 실행되고 결과가 캐싱된다.   
- @Cacheable과의 차이점은 @Cacheable은 Cache Hit인 경우 메서드 실행을 건너뛰지만 @CachPut은 실제로 메서드를 실행한 다음 그 결과를 캐싱한다.
~~~
@PutMapping("/{id}")
@CachePut(key = "#id", cacheNames = "Member")
public Member update(@PathVariable Long id, @RequestBody Member member) {
    Member findMember = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 멤버는 존재하지 않습니다."));

    findMember.update(member);
    memberRepository.save(findMember);

    log.info("Member update  :" + id);
    return findMember;
}
~~~

<br>

## @Caching
이 애노테이션은 동일한 캐싱 애노테이션을 여러 개 그룹화하여 사용할 수 있다. @Cacheable, @CacheEvict, @CachePut 을 함께 사용할 수는 없다.
~~~
@DeleteMapping("/{id}")
@Caching(evict = {
    @CacheEvict(key = "#id", cacheNames = "Member"),
    @CacheEvict(key = "#id", cacheNames = "Product")
})
public String deleteMebmerAndProduct(@PathVariable Long id) {...}
~~~

<br>

## Spring Cache 키 생성시 유의사항
int, long 과 같은 primitive type의 파라미터만 사용하는 경우가 아니고 엔티티 객체나 VO(또는 Map)를 파라미터로 사용한다면 VO가 가진 모든 feild 값을 키로 사용하는 경우는 많지 않을 것이다. 그럴 때는 아래와 같은 방법으로 키를 생성해서 사용할 수 있다.
~~~
public class MemberController {
    @PostMapping("/keygen")
	@Cacheable(key = "T(com.example.springcache.util.KeyGen).generate(#member)", cacheNames = "Member")
	public Member saveKeyGen(@RequestBody Member member){
		log.info("save to cache :" + member);
		return memberRepository.save(member);
	}
}

public class KeyGen {
	public static Object generate(Member member) {
		return member.getId() + ":" + member.getName();
	}
}
~~~