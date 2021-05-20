package kr.co.loosie.foody.application;

import kr.co.loosie.foody.domain.Reservation;
import kr.co.loosie.foody.domain.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.verify;

class ReservationServiceTests {


    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    public void getReservation(){
        long restaurantId = 1004L;
        List<Reservation> reservations
                = reservationService.getReservations(restaurantId);


        verify(reservationRepository).findAllByRestaurantId(restaurantId);
    }
}