package edu.sjsu.cmpe275;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.sjsu.cmpe275.controller.FlightController;
import edu.sjsu.cmpe275.controller.PassengerController;
import edu.sjsu.cmpe275.controller.ReservationController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The type Lab 2 application tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Lab2ApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerController passengerController;

    @Autowired
    private FlightController flightController;

    @Autowired
    private ReservationController reservationController;


    /**
     * Passenger contex loads.
     *
     * @throws Exception the exception
     */
// SMOKE TESTS
    @Test
    public void passengerContexLoads() throws Exception {
        assertThat(passengerController).isNotNull();
    }

    /**
     * Flight contex loads.
     *
     * @throws Exception the exception
     */
    @Test
    public void flightContexLoads() throws Exception {
        assertThat(flightController).isNotNull();
    }

    /**
     * Reservation contex loads.
     *
     * @throws Exception the exception
     */
    @Test
    public void reservationContexLoads() throws Exception {
        assertThat(reservationController).isNotNull();
    }


    /**
     * Should return status ok for correct passenger id.
     *
     * @throws Exception the exception
     */
// PASSENGER CRUD APIs
    @Test
    public void shouldReturnStatusOKForCorrectPassengerId() throws Exception {
        this.mockMvc.perform(get("/passenger/44e8db8c-fee0-4e7a-afe2-aef599aa4d39")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status ok with expected name for correct passenger id.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKWithExpectedNameForCorrectPassengerId() throws Exception {
        this.mockMvc.perform(get("/passenger/44e8db8c-fee0-4e7a-afe2-aef599aa4d39")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Noel")));
    }

    /**
     * Should return status 400 bad request for in correct param state.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus400BadRequestForInCorrectParamState() throws Exception {
        this.mockMvc.perform(get("/passenger/44e8db8c-fee0-4e7a-afe2-aef599aa4d39?xml=false")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status 404 not found for in correct passenger id.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus404NotFoundForInCorrectPassengerId() throws Exception {
        this.mockMvc.perform(get("/passenger/44444444444444444")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok for passenger creation.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForPassengerCreation() throws Exception {
        this.mockMvc.perform(post("/passenger?firstname=Jimmy&lastname=Bean&age=21&gender=male&phone=1444")).
                andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status ok for passenger updation.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForPassengerUpdation() throws Exception {
        this.mockMvc.perform(put("/passenger/44e8db8c-fee0-4e7a-afe2-aef599aa4d39?firstname=Val" +
                "&lastname=Hausen&age=41&gender=male&phone=13233555")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 400 bad request for passenger with same phone number.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus400BadRequestForPassengerWithSamePhoneNumber() throws Exception {
        this.mockMvc.perform(post("/passenger?firstname=Noel&lastname=D&age=21&gender=male&phone=146")).
                andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok for passenger deletion.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForPassengerDeletion() throws Exception {
        this.mockMvc.perform(delete("/passenger/87be9389-ea17-4e9c-85e2-ab8c764c27c7")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 404 not found for incorrect passenger id.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus404NotFoundForIncorrectPassengerId() throws Exception {
        this.mockMvc.perform(delete("/passenger/22222222222")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok for correct flight number.
     *
     * @throws Exception the exception
     */
// FLIGHT CRUD APIs
    @Test
    public void shouldReturnStatusOKForCorrectFlightNumber() throws Exception {
        this.mockMvc.perform(get("/flight/AA11")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 404 not found for in correct flight number.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus404NotFoundForInCorrectFlightNumber() throws Exception {
        this.mockMvc.perform(get("/flight/ZZ")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok with expected from for flight creation.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKWithExpectedFromForFlightCreation() throws Exception {
        this.mockMvc.perform(post("/flight/JJ11?price=3330&from=CA&to=BLR&departureTime=2033-12-25-09" +
                "&arrivalTime=2033-12-25-14&description=BA&capacity=140&model=KK&manufacturer=II&yearOfManufacture=1997"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("CA")));
    }

    /**
     * Should return status ok for flight updation.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForFlightUpdation() throws Exception {
        this.mockMvc.perform(post("/flight/JJ11?price=3330&from=CA&to=BLR&departureTime=2033-12-25-09" +
                "&arrivalTime=2033-12-25-16&description=BA&capacity=140&model=KK&manufacturer=II&yearOfManufacture=2000"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 400 bad request for negative flight capacity.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus400BadRequestForNegativeFlightCapacity() throws Exception {
        this.mockMvc.perform(post("/flight/LL11?price=1400&from=CA&to=BLR&departureTime=2017-12-25-09" +
                "&arrivalTime=2017-12-25-14&description=BA&capacity=-10&model=KK&manufacturer=II&yearOfManufacture=1997"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok for flight deletion.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForFlightDeletion() throws Exception {
        this.mockMvc.perform(delete("/airline/JJ11")).andDo(print())
                .andExpect(status().isOk());
    }


    /**
     * Should return status ok for correct order no.
     *
     * @throws Exception the exception
     */
// RESERVATIION CRUD APIs
    @Test
    public void shouldReturnStatusOKForCorrectOrderNo() throws Exception {
        this.mockMvc.perform(get("/reservation/e7a1a508-4d75-404e-90dd-00aa6d44400e")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 404 not found for in correct order no.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus404NotFoundForInCorrectOrderNo() throws Exception {
        this.mockMvc.perform(get("/reservation/11111111")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    /**
     * Should return status ok for reservation creation.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForReservationCreation() throws Exception {
        this.mockMvc.perform(post("/reservation?passengerId=87be9389-ea17-4e9c-85e2-ab8c764c27c7&flightLists=GG44")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status ok for reservation deletion.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatusOKForReservationDeletion() throws Exception {
        this.mockMvc.perform(delete("/reservation/68ec02f0-3376-4c99-bc04-e89cca060588")).andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * Should return status 404 not fround for incorrect reservation id.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldReturnStatus404NotFroundForIncorrectReservationId() throws Exception {
        this.mockMvc.perform(delete("/reservation/333333333333")).andDo(print())
                .andExpect(status().is4xxClientError());
    }


}
