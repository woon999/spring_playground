# Log4j2 사용해보기 
- Thread: 8
- ramp-up time: 1
- loop: 8
- API Request 1개 당 Log 10,000개 기록

 | |Sync | AsyncAppender | AsyncLogger |
|------|------|---|---|
|처리랑|8.4t/s|9.9t/s|85.9t/s|


## Sync
- 처리량: 8.4t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <RollingFile name="File"
                     fileName="./logs/app.log"
                    filePattern="./logs/app.%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="${LOG_PATTERN}" charset="UTF-8"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="10MB" />
            </Policies>
            <DefaultRolloverStrategy max="20" fileIndex="min" />
        </RollingFile>

    <Loggers>
        <logger name="com.example.springlog4j2" level="debug" additivity="false">
<!--         Sync   8.4t/s-->
           <AppenderRef ref="File"/>
        </logger>
        <Root level="info">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>

</Configuration>
~~~
<img width="1064" alt="스크린샷 2022-05-13 오후 11 09 05" src="https://user-images.githubusercontent.com/54282927/168304270-3ac1eea6-5e2e-4858-aa62-f83054e49dea.png">

## async appender
- 처리량: 9.9t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

        <RollingFile name="File"
                     fileName="./logs/app.log"
                    filePattern="./logs/app.%d{yyyy-MM-dd}-%i.log">
            <PatternLayout pattern="${LOG_PATTERN}" charset="UTF-8"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="10MB" />
            </Policies>
            <DefaultRolloverStrategy max="20" fileIndex="min" />
        </RollingFile>

<!--        AsyncAppender-->
        <Async name="AsyncAppender">
            <AppenderRef ref="File"/>
        </Async>
    <Loggers>
        <logger name="com.example.springlog4j2" level="debug" additivity="false">
<!--            Async Appender 9.9t/s -->
            <AppenderRef ref="AsyncAppender"/>
        </logger>
        <Root level="info">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>

</Configuration>
~~~
<img width="1060" alt="스크린샷 2022-05-13 오후 11 06 08" src="https://user-images.githubusercontent.com/54282927/168304279-5edfdf7f-5727-446d-83aa-9653e910e0b6.png">

## async logger
- 처리량: 85.9t/s
~~~
<?xml version="1.0" encoding="UTF-8" ?>
<Configuration status="INFO">
    <Properties>
        <Property name="LOG_PATTERN">[%d{yyyy-MM-dd HH:mm:ss}:%-3relative] %-5level ${PID:-} --- [%15.15thread] %-40.40logger{36} : %msg%n</Property>
    </Properties>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>

<!--        Async Logger-->
        <RollingRandomAccessFile name="RandomAccessFile" fileName="./logs/app.log"
                          filePattern="./logs/app.%d{yyyy-MM-dd}-%i.log"
                          immediateFlush="false" append="false">
            <PatternLayout pattern="${LOG_PATTERN}" charset="UTF-8"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
                <SizeBasedTriggeringPolicy size="10MB" />
            </Policies>
            <DefaultRolloverStrategy max="20" fileIndex="min" />
        </RollingRandomAccessFile>
    </Appenders>

    <Loggers>
        <logger name="com.example.springlog4j2" level="debug" additivity="false">
<!--          Async Logger 85.7t/s -->
            <AppenderRef ref="RandomAccessFile"/>
        </logger>
        <Root level="info">
            <AppenderRef ref="console"/>
        </Root>
    </Loggers>
</Configuration>
~~~
<img width="1057" alt="스크린샷 2022-05-13 오후 11 23 16" src="https://user-images.githubusercontent.com/54282927/168304585-754077c8-a828-4058-a5f0-0651d5b987b5.png">
