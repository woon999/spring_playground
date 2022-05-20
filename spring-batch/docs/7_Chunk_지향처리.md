# 7. Chunk 지향 처리
## Chunk
chunk 사전적 용어는 "한 호흡에 말하는 길이"로, Spring Batch에서의 chunk란 데이터 덩어리로 작업 할 때 각 커밋 사이에 처리되는 row 수를 얘기한다. 

<br>

## Chunk 지향 처리
Chunk 지향 처리란 한 번에 하나씩 데이터를 읽어 hunk라는 덩어리를 만든 뒤, Chunk 단위로 트랜잭션을 다룬다.
물론 Chunk 단위로 트랜잭션을 수행하기 때문에 실패할 경우엔 해당 Chunk 만큼만 롤백이 되고, 이전에 커밋된 트랜잭션 범위까지는 반영이 된다.

다음 그림은 Chunk 단위로 실행하는 플로우를 보여준다.
- Reader와 Processor에서는 1건씩 다뤄지고, Writer에선 Chunk 단위로 처리된다.
  
<img width="610" alt="스크린샷 2022-05-19 오후 11 54 36" src="https://user-images.githubusercontent.com/54282927/169326605-d9c0c12b-0e63-4bef-b27e-76d665a421c2.png">

<br>

## 7-1. ChunkOrientedTasklet 엿보기 
Chunk지향 처리의 전체 로직을 다루는 것은 `ChunkOrientedTasklet` 클래스이다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 03 05" src="https://user-images.githubusercontent.com/54282927/169328401-01b7fa97-f8f6-455c-8a76-372cbfb4aa06.png">

<br>

### execute() 메서드
- `chunkProvider.provide()`로 Reader에서 Chunk size만큼 데이터를 가져온다
- `chunkProcessor.process()`에서 Reader로 받은 데이터를 가공(Processor)하고 저장(Writer)한다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 34 02" src="https://user-images.githubusercontent.com/54282927/169338064-faed7a41-fc12-4eab-85bd-f6630f721299.png">

<br>

### SimpleChunkProvider의 provide() 메서드 
데이터를 가져오는 chunkProvider의 가장 기본적인 구현체 SimpleChunkProvider의 provide()를 살펴보자.
- inputs이 ChunkSize만큼 쌓일때까지 read()를 호출한다.
- read() 내부에는  ItemReader.read를 호출한다.
- **즉, ItemReader.read에서 1건씩 데이터를 조회해 Chunk size만큼 데이터를 쌓는 것이 provide()가 하는 일이다**

<img width="700" alt="스크린샷 2022-05-20 오전 12 55 26" src="https://user-images.githubusercontent.com/54282927/169344506-27a6b41c-badf-4d37-97bf-22d5acf91acc.png">

<br>

### SimpleChunkProcessor의 process() 메서드  
Processor와 Writer 로직을 담고 있는 것은 ChunkProcessor가 담당한다. 
가장 기본적인 구현체 SimpleChunkProcessor의 process() 메서드르 살펴보자.
- Chunk inputs를 파라미터로 받는다. 이 데이터는 앞서 chunkProvider.provide() 에서 받은 ChunkSize만큼 쌓인 item이다.
- transform() 에서는 전달 받은 inputs을 doProcess()로 전달하고 변환된 값을 받는다. 
	- doProcess() 내부에는 ItemProcessor의 process()를 사용한다. 
	- transform()을 통해 가공된 대량의 데이터는 write()를 통해 일괄 저장된다. 
- write()는 저장이 될수도 있고, 외부 API로 전송할 수 도 있다. 
	- 이는 개발자가 ItemWriter를 어떻게 구현했는지에 따라 달라진다.

<img width="800" alt="스크린샷 2022-05-20 오전 12 59 14" src="https://user-images.githubusercontent.com/54282927/169345338-856bcc5f-6aff-4211-9a4a-60cbd3a11bad.png">

<br>


이렇게 가공된 데이터들은 SimpleChunkProcessor의 doWrite() 메서드를 호출하여 일괄 처리(배치 프로세싱)한다.
- writeItems(items) -> ItemWriter.write(items)

<br>

## 7-2. Page Size vs Chunk Size
PagingItemReader에서 사용하는 Page Size와 Chunk Size는 서로 의미하는 바가 다르다.
- Chunk Size는 한번에 처리될 트랜잭션 단위를 나타낸다. 
- Page Size는 한번에 조회할 Item의 Size를 나타낸다.

<br>

### 두 값이 다르다면?
PageSize가 10이고, ChunkSize가 50이라면 ItemReader에서 Page 조회가 5번 일어나면 1번의 트랜잭션이 발생하여 Chunk가 처리된다.
한번의 트랜잭션 처리를 위해 5번의 쿼리 조회가 발생하기 때문에 성능상 이슈가 발생할 수 있다. 그래서 Spring Batch의 PagingItemReader에는 클래스 상단에 다음과 같은 주석을 남겨두었다.

~~~
Setting a fairly large page size and using a commit interval that matches the page size should provide better performance.

Page size를 크게 설정하고 해당 사이즈와 일치하는 커밋 간격(Chunk size)를 사용하면 더 나은 성능을 제공한다.
~~~

<br>

성능상 이슈 외에도 2개 값을 다르게 할 경우 JPA를 사용한다면 영속성 컨텍스트가 깨지는 문제도 발생한다고 한다. [(참고)](https://jojoldu.tistory.com/146)

<br>  

따라서, 2개 값이 의미하는 바가 다르지만 여러 이슈로 2개 값을 동일하게 설정하는 것이 좋다.
  