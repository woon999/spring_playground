# 10. ItemProcessor
ItemProcessor는 데이터를 가공 (혹은 처리)한다. 해당 기능은 필수가 아니다.
- ItemProcessor는 데이터를 가공하거나 필터링하는 역할을 한다. 이는 Writer 부분에서도 충분히 구현 가능하다.
- 그럼에도 ItemProcessor를 쓰는 것은 Reader, Writer와는 별도의 단계로 기능이 분리되기 때문이다.

<br>

ChunkSize 단위로 묶은 데이터를 한번에 처리하는 ItemWriter와는 달리 ItemProcessor는 Reader에서 넘겨준 데이터 개별 건을 가공 및 처리한다.
일반적은 ItemProcessor를 사용하는 방법은 2가지이다.
- 변환: Reader에서 읽은 데이터를 원하는 타입으로 변환해서 Writer에 넘긴다.
- 필터: Reader에서 넘겨준 데이터를 Writer로 넘겨줄 것인지를 결정할 수 있다. null을 반환하면 Writer에 전달되지 않는다.


<br>

## 기본 사용법
- I: ItemReader에서 받을 데이터 타입
- O: ItemWriter에 보낼 데이터 타입
~~~
public interface ItemProcessor<I, O> {
  O process(I item) throws Exception;
}
~~~ 




