package kr.co.loosie.foody.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CategoryTests {

    @Test
    public void creation(){
        Category category = Category.builder().name("Korean Food").build();

        assertThat(category.getName(),is("Korean Food"));
    }
}