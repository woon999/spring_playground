# Spring 프로젝트 Docker 배포 (로컬, AWS EC2) 배포
## 환경설정
- SpringBoot 2.6.4
- Java 11
- Gradle 7.4.1

<br>

## 1. 로컬에서 Spring 프로젝트 Docker로 배포
### 1-1. Dockerfile 파일 생성
~~~
FROM openjdk:11
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
~~~ 
`FROM <image>:<tag>` : 베이스 이미지 지정

`EXPOSE`: Dockerfile의 빌드로 생성된 이미지에서 열어줄 포트를 의미한다.

`VOLUME`: 파일들을 컨테이너로 복사하지않고 참조하도록 설정

`ARG ...`: build가 되는 시점에 *JAR_FILE*이라는 변수명에 *build/libs/\*.jar* 표현식을 선언함 

`COPY <src> <dest>`: build 명령 중간에 호스트의 파일 또는 폴더를 이미지에 가져오는 것. 파일이나 디렉토리를 이미지로 복사한다.
  
`ENTRYPOINT`: docker run 실행시 실행되는 명령어이다. ["java", "-jar", "app.jar"]  => "java -jar app.jar"
 
<br>
 
 ### 1-2. 로컬에서 배포하기
 ~~~
 $ docker build -t {image-name} .
 $ docker run -p 8080:8080 {image-name}
---
 $ docker build -t app.jar .
 $ docker run -p 8080:8080 app.jar
~~~

<br>
 
## 2. AWS EC2로 배포하기 
### 2-1. docker hub에 repo 생성
https://hub.docker.com/repository/docker/jong9712/spring-docker-practice


<br>

### 2-2. docker hub로 push하기
~~~
$ docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
$ docker push [OPTIONS] NAME[:TAG]
---
$ docker tag app.jar jong9712/spring-docker-practice:version1.0
$ docker push jong9712/spring-docker-practice:version1.0
~~~

<br>

### 2-3. EC2 인스턴스 생성
AWS EC2 인스턴스 생성 후 접속하기 👉[클릭](loosie.tistory.com/407)

<img width="852" alt="스크린샷 2022-03-17 오후 9 34 54" src="https://user-images.githubusercontent.com/54282927/158811717-2c3276ef-b3c4-4bdb-9945-407a48d6e5f1.png">


<br>

### 2-4. EC2에 Docker 설치 및 실행
~~~
$ sudo yum install docker # 모두 y
$ sudo systemctl start docker # 도커 실행 
$ sudo docker pull jong9712/spring-docker-practice:version1.0 # 도커 이미지 pull
$ sudo docker run -p 8080:8080 jong9712/spring-docker-practice:version1.0 # 8080포트로 스프링 부트 실
~~~
<img width="925" alt="스크린샷 2022-03-17 오후 9 48 56" src="https://user-images.githubusercontent.com/54282927/158811794-0300c7e8-4d9f-46e9-904f-e686e942212c.png">


<br>

### ref
- https://www.youtube.com/watch?v=e3YERpG2rMs
