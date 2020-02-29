package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.RestaurantService;
import kr.co.loosie.foody.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;

@RestController
public class RestaurantController {



    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<Restaurant> list(){

        List<Restaurant> restaurants = restaurantService.getRestaurants();

        return restaurants;
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant detail(@PathVariable("id") Long id){
        Restaurant restaurant = restaurantService.getRestaurant(id);
//      기본 메뉴 + 메뉴 정보



        return restaurant;

    }

}
