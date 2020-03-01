package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.MenuItem;
import kr.co.loosie.foody.domain.MenuItemRepository;
import kr.co.loosie.foody.domain.Restaurant;
import kr.co.loosie.foody.domain.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }

    public Restaurant getRestaurant(Long id){
        Restaurant restaurant = restaurantRepository.findById(id);

           List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantId(id);
           restaurant.setMenuItem(menuItems);
        return restaurant;
    }


    public Restaurant addRestaurant(Restaurant restaurant) {

        return restaurantRepository.save(restaurant);
    }
}
