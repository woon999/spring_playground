package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTests {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private ReservationService reservationService;


    @Test
    public void create() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDQsIm5hbWUiOiJKb2huIn0.8hm6ZOJykSINHxL-rf0yV882fApL3hyQ9-WGlJUyo2A";

        Long userId = 1004L;
        String name = "John";
        String date = "2019-12-24";
        String time = "20:00";
        Integer partySize= 20;



        mvc.perform(post("/restaurants/369/reservations")
                    .header("Authorization","Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"date\":\"2019-12-24\",\"time\":\"20:00\"," +
                            "\"partySize\":20}"))
              .andExpect(status().isCreated());

        verify(reservationService).addReservation(
               eq(369L), eq(userId), eq(name), eq(date), eq(time), eq(partySize));
    }


}