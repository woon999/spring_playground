package com.example.springbatch.job;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Decider 사용해보기
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeciderJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job deciderJob() {
		return jobBuilderFactory.get("deciderJob")
			.start(startStep())
			.next(decider()) // 홀짝을 구분하는 decider
			.from(decider()) //  만약 decider 상태 값이 홀수(ODD)라면
			.on("ODD")
			.to(oddStep()) // oddStep으로 이동
			.from(decider()) // 만약 상태 값이 짝수(EVEN)라면
			.on("EVEN")
			.to(evenStep()) // evenStep으로 이동
			.end()
			.build();
	}

	@Bean
	public Step startStep() {
		return stepBuilderFactory.get("startStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> step start");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step evenStep() {
		return stepBuilderFactory.get("evenStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> evenStep process");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public Step oddStep() {
		return stepBuilderFactory.get("oddStep")
			.tasklet((contribution, chunkContext) -> {
				log.info(">>>>> oddStep process");
				return RepeatStatus.FINISHED;
			})
			.build();
	}

	@Bean
	public JobExecutionDecider decider() {
		return new OddDecider();
	}

	public static class OddDecider implements JobExecutionDecider {

		@Override
		public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
			Random rand = new Random();

			int randomNumber = rand.nextInt(50) + 1;
			log.info("randomNumber: {}", randomNumber);

			if(randomNumber % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			} else {
				return new FlowExecutionStatus("ODD");
			}
		}
	}
}
