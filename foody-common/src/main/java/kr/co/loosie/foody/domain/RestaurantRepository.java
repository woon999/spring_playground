package kr.co.loosie.foody.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface    RestaurantRepository extends CrudRepository<Restaurant,Long> {
    List<Restaurant> findAll();

    List<Restaurant> findAllByAddressContainingAndCategoryId(
            String seoul, Long categoryId);

    //  Optional은 null을 처리하지 않고, restaurant가 있냐 없냐로 구분
    Optional<Restaurant> findById(Long id);

    Restaurant save(Restaurant restaurant);
}
