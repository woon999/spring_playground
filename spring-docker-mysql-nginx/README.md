# Spring + Docker + Mysql + Nginx 배포하기

## nginx 잘 동작하는지 확인하기
### 1. docker로 nginx 실행
- Mac OS 경우 /etc/apache2/httpd.conf파일에 들어가서 Listen 80 주석 처리해줘야 됨
~~~
$ docker run -p 80:80 nginx # nginx 실행
~~~
### 2. http://localhost로 확인
"Welcome to nginx!" 페이지뜨면 확인 

<br>

## spring 프로젝트에 맞춰 nginx 설정 추가하기 
app.conf
~~~
server{
  listen 80;
  charset utf-8;
  access_log off;

  location / {
    proxy_pass http://spring-nginx-mysql-app:8080;
      proxy_set_header Host $host:$server_port;
    proxy_set_header X-Forwarded-Host $server_name;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  }

  location /static {
    access_log off;
    expires 30d;

    alias /app/static;
  }
}

~~~

<br>

## docker-compose로 한번에 배포
docker-compose.yml
~~~
version: "3"

services:
  mysql:
    image: mysql
    container_name: mysqldb
    environment:
      - MYSQL_DATABASE=test
      - MYSQL_ROOT_PASSWORD=root
    ports:
      - 3307:3306
    volumes:
      - app-db-data:/var/lib/mysql
    networks:
      - spring-net

  spring-nginx-mysql-app:
    image: spring-nginx-mysql-app
    container_name: app
    build: .
    restart: always
    environment:
      MYSQL_HOST: mysqldb
      MYSQL_USER: root
      MYSQL_PASSWORD: root
      MYSQL_PORT: 3306
    ports:
      - 8080:8080
    depends_on:
      - mysql
    networks:
      - spring-net

  nginx:
    container_name: spring-nginx
    image: nginx
    restart: always
    ports:
      - 80:80
    volumes:
      - ./nginx/conf.d:/etc/nginx/conf.d
    depends_on:
      - spring-nginx-mysql-app
    networks:
      - spring-net

volumes:
  app-db-data:

networks:
  spring-net:
~~~

<br>


## 실행 후 테스트
- Run command `docker-compose up`
- Access to `http://localhost`
- postman으로 데이터 넣기 
- Access to `http://localhost/products`




<br>

ref
- https://hellokoding.com/docker-compose-with-spring-boot-mysql-nginx/