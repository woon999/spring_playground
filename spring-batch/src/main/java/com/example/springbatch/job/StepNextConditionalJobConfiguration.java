package com.example.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.item.ChunkOrientedTasklet;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.core.step.item.SimpleChunkProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 조건 분기해서 사용해보기
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class StepNextConditionalJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	// ChunkOrientedTasklet
	// SimpleChunkProvider
	SimpleChunkProcessor
	@Bean
	public Job stepNextConditionJob(){
		return jobBuilderFactory.get("stepNextConditionJob") // "stepNextConditionJob"이름을 가진 Batch Job 생성
			.start(conditionJobStep1())
				.on("FAILED") // ExitStatus가 FAILED일 경우
				.to(conditionJobStep3()) // step3로 이동
				.on("*") // step3 결과와 상관없이(모든 경우)
				.end() // flow 종료
			.from(conditionJobStep1())
				.on("*")  // ExitStatus가 FAILED가 아닌 모든 경우 if-else의 else 구문
				.to(conditionJobStep2()) // step2로 이동
				.next(conditionJobStep3()) // step2가 정상 종료되면 step3로 이동
				.on("*") // 모든 경우
				.end() // flow 종료
			.end() // job 종료
			.build();
	}

	@Bean
	public Step conditionJobStep1(){
		return stepBuilderFactory.get("step1")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [1] --- stepNextConditionJob1 process");

				/**
				 * ExitStatus를 FAILED로 지정한다.
				 * 해당 status를 보고 flow가 진행된다.
				 */
				// contribution.setExitStatus(ExitStatus.FAILED);
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step conditionJobStep2(){
		return stepBuilderFactory.get("step2")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [2] --- stepNextConditionJob2 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}

	@Bean
	public Step conditionJobStep3(){
		return stepBuilderFactory.get("step3")
			.tasklet(((contribution, chunkContext) -> {
				log.info(">>>>> [3] --- stepNextConditionJob3 process");
				return RepeatStatus.FINISHED;
			}))
			.build();
	}
}
