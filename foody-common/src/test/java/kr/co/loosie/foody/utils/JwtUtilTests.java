package kr.co.loosie.foody.utils;


import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

class JwtUtilTests {

    @Test
    public void createToken(){
        JwtUtil jwtUtil = new JwtUtil();

        String token = jwtUtil.createToken(1004L,"John");

        assertThat(token,containsString("."));
    }

}