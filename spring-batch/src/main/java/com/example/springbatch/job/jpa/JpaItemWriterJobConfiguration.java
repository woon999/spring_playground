package com.example.springbatch.job.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springbatch.domain.Pay;
import com.example.springbatch.domain.PayWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;

	private final int CHUNK_SIZE = 10;

	@Bean
	public Job jpaItemWriterJob(){
		return jobBuilderFactory.get("jpaItemWriterJob")
			.start(jpaItemWriterStep())
			.build();
	}

	@Bean
	public Step jpaItemWriterStep(){
		return stepBuilderFactory.get("jpaItemWriterStep")
			.<Pay, PayWriter>chunk(CHUNK_SIZE)
			.reader(jpaItemWriterReader())
			.processor(jpaItemProcessor())
			.writer(jpaItemWriter())
			.build();
	}

	@Bean
	public JpaPagingItemReader<Pay> jpaItemWriterReader() {
		return new JpaPagingItemReaderBuilder<Pay>()
			.name("jpaItemWriterReader")
			.entityManagerFactory(entityManagerFactory)
			.pageSize(CHUNK_SIZE)
			.queryString("SELECT p FROM Pay p")
			.build();
	}

	@Bean
	public ItemProcessor<Pay, PayWriter> jpaItemProcessor() {
		return pay -> new PayWriter(pay.getAmount(), pay.getTxName(), pay.getTxDateTime());
	}

	@Bean
	public JpaItemWriter<PayWriter> jpaItemWriter() {
		JpaItemWriter<PayWriter> jpaItemWriter = new JpaItemWriter<>();
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}
}
