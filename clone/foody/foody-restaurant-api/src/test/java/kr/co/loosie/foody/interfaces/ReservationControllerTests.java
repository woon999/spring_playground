package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationController.class)
class ReservationControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationService reservationService;


    @Test
    public void list() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIwMjAsIm5hbWUiOiJKb2huIiwicmVzdGF1cmFudElkIjoxMDA0fQ.h30V0x_Eq7Jk_b6KZRDt-92t6fD1Qyankwh5cu3Xyxs";


        mvc.perform(get("/reservations")
                .header("Authorization","Bearer " + token))
                .andExpect(status().isOk());


        verify(reservationService).getReservations(1004L);
    }


}