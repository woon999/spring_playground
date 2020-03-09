package kr.co.loosie.foody.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class UserTests {

    @Test
    public void creation(){
        User user = User.builder()
                .email("tester@example.com")
                .name("테스터")
                .level(100L)
                .build();

        assertThat(user.getName(),is("테스터"));
        assertThat(user.isAdmin(),is(true));
        assertThat(user.isActive(),is(true));

        user.deactivate();

        assertThat(user.isActive(),is(false));

    }
}