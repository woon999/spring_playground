package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.MenuItem;
import kr.co.loosie.foody.domain.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }


    public void bulkUpdate(Long restaurantId, List<MenuItem> menuItems) {
        for(MenuItem menuItem : menuItems){
            menuItem.setRestaurantId(restaurantId);
            menuItemRepository.save(menuItem);
        }

        }
}
