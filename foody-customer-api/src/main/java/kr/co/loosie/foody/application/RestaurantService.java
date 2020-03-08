package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RestaurantService {

    private RestaurantRepository restaurantRepository;
    private MenuItemRepository menuItemRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository,
                             ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Restaurant> getRestaurants(String region) {
        //TODO : region으로 필터링
        List<Restaurant> restaurants = restaurantRepository.findAllByAddressContaining(region);
        return restaurants;
    }

    public Restaurant getRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantId(id);
        restaurant.setMenuItem(menuItems);

        List<Review> reviews = reviewRepository.findAllByRestaurantId(id);
        restaurant.setReviews(reviews);

        return restaurant;
    }


    public Restaurant addRestaurant(Restaurant restaurant) {

        return restaurantRepository.save(restaurant);
    }


    @Transactional
    public Restaurant updateRestaurant(long id, String name, String address) {
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);

        restaurant.updateInformation(name, address);


        return restaurant;
    }
}
