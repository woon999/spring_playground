package com.example.springcm4sb;

import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcm4sb.model.Post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InitData {
	private final InitService initService;

	/**
	 * 10만개 insert 52237ms
	 * batch size 1000 : 47424ms
	 * batch + order_insert: 37378ms
	 * https://velog.io/@rainmaker007/spring-data-jpa-batch-insert-%EC%A0%95%EB%A6%AC
	 */
	@PostConstruct
	void init() {
		long start = System.currentTimeMillis();
		// log.info(" ------- start ---- " + start);
		// initService.init();
		// log.info(" ------- end ---- " + (System.currentTimeMillis()-start));
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {
		private final EntityManager em;

		public void init() {
			for (int i = 0; i < 100_000; i++) {
				Post post = Post.builder()
					.content("content" + i)
					.link("http://test.com" + i)
					.title("title" + i)
					.writer("writer" + i)
					.build();
				em.persist(post);
			}
		}

	}

}
