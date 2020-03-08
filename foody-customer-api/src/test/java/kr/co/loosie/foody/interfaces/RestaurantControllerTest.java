package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.RestaurantService;
import kr.co.loosie.foody.domain.MenuItem;
import kr.co.loosie.foody.domain.Restaurant;
import kr.co.loosie.foody.domain.RestaurantNotFoundException;
import kr.co.loosie.foody.domain.Review;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    //  controller가 repository에 직접적으로 의존하고 있었는데
//  이렇게 Impl과 inteface로 나누어 의존성을 분리를 통해 의존성주입의 장점을 살림
//    (RestaurantRepositoryImpl.class) 우리가 사용하는 객체를 다양하게 변경가능
 /*   @SpyBean(RestaurantRepositoryImpl.class)
    private RestaurantRepository restaurantRepository;

    @SpyBean(MenuItemRepositoryImpl.class)
    private MenuItemRepository menuItemRepository;
*/


    @Test
    public void list() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(1004L)
                .name("Joker House")
                .address("Seoul")
                .build());
        given(restaurantService.getRestaurants("Seoul"))
                .willReturn(restaurants);

        mvc.perform(get("/restaurants?region=Seoul"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")))
                .andExpect(content().string(
                        containsString("\"name\":\"Joker House\"")));

    }

    @Test
    public void detailWithExisted() throws Exception {
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Joker House")
                .address("Seoul")
                .build();

        MenuItem menuItem = MenuItem.builder()
                .name("Kimchi")
                .build();
        restaurant.setMenuItem(Arrays.asList(menuItem));
        Review review = Review.builder()
                .name("JOKER")
                .score(5)
                .description("good")
                .build();

        restaurant.setReviews(Arrays.asList(review));

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);

        mvc.perform(get("/restaurants/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Joker House\"")
                ))
                .andExpect(content().string(
                        containsString("Kimchi")
                ))
                .andExpect(content().string(
                        containsString("good")
                ));

    }

    @Test
    public void detailWithNotExisted() throws Exception {
            given(restaurantService.getRestaurant(404L)).
                    willThrow(new RestaurantNotFoundException(404L));

            mvc.perform(get("/restaurants/404"))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("{}"));
    }




}