package com.example.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SimpleJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job simpleJob(){
		return jobBuilderFactory.get("simpleJob") // "simpleJob"이름을 가진 Batch Job 생성
			.start(simpleStep1(null))
			.next(simpleStep2(null))
			.build();
	}

	/**
	 *  Batch가 수행되면 log를 출력하는 step 생성
	 *
	 * @JobScope와 @StepScope
	 * 두 애노테이션은 스프링의 기본 Scope인 싱글톤 방식과는 대치되는 역할이다.
	 * - Bean의 생성 시점이 스프링 애플리케이션이 실행되는 시점이 아닌 @JobScope, @StepScope가 명시된 메서드가 실행될 때까지 지연시키는 것을 의미한다.
	 * - 이러한 행위를 Late Binding이라고도 한다.
	 *
	 * JobParameters는 아래 예제코드처럼 @Value를 통해서 가능하다.
	 * - JobPameters는 Step이나 Tasklet, Reader 등 배치 컴포넌트 Bean의 생성 시점에 호출할 수 있다. 정확하게 말해서 Scope Bean을 생성할때만 가능하다.
	 * - 동일한 Job이 Job Parameter가 달라지면 그때마다 BATCH_JOB_INSTANCE에 생성되며, 동일한 Job Parameter는 여러개 존재할 수 없다 .
	 */
	@Bean
	@JobScope
	public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate){
		return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step1 process");
				log.info(">>>>> request Date: {}", requestDate);
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	@JobScope
	public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate){
		return stepBuilderFactory.get("simpleStep2") // "simpleStep1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step2 process");
				log.info(">>>>> request Date: {}", requestDate);
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
