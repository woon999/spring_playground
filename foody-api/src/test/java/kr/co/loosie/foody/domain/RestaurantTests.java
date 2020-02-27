package kr.co.loosie.foody.domain;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestaurantTests {

    @Test
    public void creation(){
        Restaurant restaurant = new Restaurant("Bob zip","Seoul");

        assertThat(restaurant.getName(), is("Bob zip"));
        assertThat(restaurant.getAddress(), is("Seoul"));
    }

    @Test
    public void info(){
        Restaurant restaurant = new Restaurant("Bob zip", "Seoul");

        assertThat(restaurant.getInfo(), is("Bob zip in Seoul"));
    }

}