package com.example.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
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
			.start(simpleStep1())
			.build();
	}

	// Batch가 수행되면 log를 출력하는 step 생성
	@Bean
	public Step simpleStep1(){
		return stepBuilderFactory.get("simpleStep1") // "simpleStep1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step1 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
