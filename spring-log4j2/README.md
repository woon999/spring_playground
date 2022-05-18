# Log4j2 사용해보기 
- Thread: 8
- ramp-up time: 1
- API Request 1개 당 Log 10,000개 기록

 | |Sync | AsyncAppender | AsyncLogger |
|------|------|---|---|
|처리랑|7.8t/s|15.9t/s|147.8t/s|


## Sync Logger
- 처리량: 7.8t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATH">logs/app</Property>
        <Property name="FILE_PATTERN">%d{yyyy-MM-dd}-%i</Property>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <RollingFile name="File"
                     fileName="${LOG_PATH}.log"
                     filePattern="${LOG_PATH}-${FILE_PATTERN}.log"
                     append="false">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                <SizeBasedTriggeringPolicy size="100MB" />
            </Policies>
            <DefaultRolloverStrategy max="20" fileIndex="min" />
        </RollingFile>

    </Appenders>
    <Loggers>
        <logger name="com.example.springlog4j2" level="info" additivity="false" includeLocation="false">
            <!--   Sync 7.8t/s-->
            <AppenderRef ref="File"/>-
        </logger>
        <Root level="info" includeLocation="false">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
~~~

<br>

### 실행 결과
<img width="1054" alt="스크린샷 2022-05-18 오후 11 52 03" src="https://user-images.githubusercontent.com/54282927/169071436-43784b7f-dd75-424c-91d0-642bfb5b5650.png">

## Async Appender
- 처리량: 15.9t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATH">logs/app</Property>
        <Property name="FILE_PATTERN">%d{yyyy-MM-dd}-%i</Property>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <RollingFile name="AsyncFile"
                     fileName="${LOG_PATH}.log"
                     filePattern="${LOG_PATH}-${FILE_PATTERN}.log"
                     immediateFlush="false" append="false">
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
                <SizeBasedTriggeringPolicy size="100MB" />
            </Policies>
            <DefaultRolloverStrategy max="20" fileIndex="min" />
        </RollingFile>

        <Async name="AsyncAppender" >
            <AppenderRef ref="AsyncFile" />
        </Async>

    </Appenders>
    <Loggers>
        <logger name="com.example.springlog4j2" level="info" additivity="false" includeLocation="false">
            <!--   Async Appender 15.9 t/s -->
            <AppenderRef ref="AsyncAppender"/>
        </logger>
        <Root level="info" includeLocation="false">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>


~~~

<br>

### 실행 결과
<img width="1061" alt="스크린샷 2022-05-18 오후 11 36 16" src="https://user-images.githubusercontent.com/54282927/169071664-004cef9c-51e6-4599-bd6e-94380f5c5072.png">

## Async Logger
- 처리량: 147.8t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATH">logs/app</Property>
        <Property name="FILE_PATTERN">%d{yyyy-MM-dd}-%i</Property>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <!-- Async Loggers will auto-flush in batches, so switch off immediateFlush. -->
        <RandomAccessFile name="RandomAccessFile"
                          fileName="${LOG_PATH}-async.log"
                          immediateFlush="false" append="false">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </RandomAccessFile>
    </Appenders>
    <Loggers>
        <logger name="com.example.springlog4j2" level="info" additivity="false" includeLocation="false">
            <!--    All Async Logger 147.8 t/s-->
            <AppenderRef ref="RandomAccessFile"/>
        </logger>
        <Root level="info" includeLocation="false">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
~~~

<br>

### 실행 결과
<img width="1058" alt="스크린샷 2022-05-18 오후 11 44 43" src="https://user-images.githubusercontent.com/54282927/169070432-4ed74440-8dfb-4c98-94a9-bebb5f050612.png">
