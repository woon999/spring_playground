package com.example.springredis;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultPopulator implements ApplicationRunner {

	@Autowired
	private MeetingRepository meetingRepository;

	@Override public void run(ApplicationArguments args) throws Exception {
		for(int i=0; i<5; i++){
			Meeting meeting = new Meeting();
			meeting.setTitle("New Meeting" + i);
			meeting.setStartAt(new Date());
			meetingRepository.save(meeting);
		}

		meetingRepository.findAll().forEach(m -> {
			System.out.println("=============");
			System.out.println(m.getTitle() + " " + m.getStartAt());
		});
	}
}
