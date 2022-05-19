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

/**
 * Next 사용해보기
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StepNextJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job stepNextJob(){
		return jobBuilderFactory.get("stepNextJob") // "stepNextJob"이름을 가진 Batch Job 생성
			.start(step1())
			.next(step2())
			.next(step3())
			.build();
	}

	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1") // "step1"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step1 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step step2(){
		return stepBuilderFactory.get("step2") // "step2"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step2 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step step3(){
		return stepBuilderFactory.get("step3") // "step3"이름을 가진 Batch Step 생성
			.tasklet(((contribution, chunkContext) -> { // step에서 수행될 기능 명시
				log.info(">>>>> step3 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
