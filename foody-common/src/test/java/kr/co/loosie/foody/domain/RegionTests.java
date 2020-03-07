package kr.co.loosie.foody.domain;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static org.junit.jupiter.api.Assertions.*;

class RegionTests {

    @Test
    public void creation(){
        Region region = Region.builder().name("서울").build();

        assertThat(region.getName(),is("서울"));
    }

}