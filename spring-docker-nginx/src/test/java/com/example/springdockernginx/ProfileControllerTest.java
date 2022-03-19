package com.example.springdockernginx;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ProfileControllerTest {

	@Test
	void profileTest_default(){
		// given
		String expectedProfile = "default";
		MockEnvironment env = new MockEnvironment();
		env.addActiveProfile(expectedProfile);
		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertEquals(profile, expectedProfile);
	}

	@Test
	void profileTest_real(){
		// given
		String expectedProfile = "real";
		MockEnvironment env = new MockEnvironment();
		env.addActiveProfile(expectedProfile);
		env.addActiveProfile("oauth");
		env.addActiveProfile("real-db");

		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertEquals(profile, expectedProfile);
	}

	@Test
	void profileTest_no_real(){
		// given
		String expectedProfile = "oauth";
		MockEnvironment env = new MockEnvironment();
		env.addActiveProfile(expectedProfile);
		env.addActiveProfile("real-db");

		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertEquals(profile, expectedProfile);
	}


}