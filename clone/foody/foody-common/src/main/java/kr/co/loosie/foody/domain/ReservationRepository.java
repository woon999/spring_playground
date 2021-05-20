package kr.co.loosie.foody.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository
        extends CrudRepository<Reservation,Long> {

    Reservation save(Reservation reservation);

    List<Reservation> findAllByRestaurantId(long restaurantId);
}
