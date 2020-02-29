package kr.co.loosie.foody.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


// Component를 붙임으로써 RestaurantRepository를 Spring이 직접 관리.
@Component
public class MenuItemRepositoryImpl implements MenuItemRepository {

    List<MenuItem> menuItems = new ArrayList<>();

    public MenuItemRepositoryImpl() {
        menuItems.add(new MenuItem("Kimchi"));
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(Long restaurantId) {
        return menuItems;
    }
}
