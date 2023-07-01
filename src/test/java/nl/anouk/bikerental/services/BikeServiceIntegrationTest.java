package nl.anouk.bikerental.services;

import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public class BikeServiceIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private BikeRepository bikeRepository;

        @Autowired
        private ReservationRepository reservationRepository;

        @Test
        public void testAreBikesAvailable() throws Exception {
            // Maak en sla fietsen op in de repository
            Bike bike1 = new Bike();
            bike1.setBrand("Bike 1");
            bike1.setRegistrationNo(455825L);
            bike1.setIsAvailable(true);
            bikeRepository.save(bike1);

            Bike bike2 = new Bike();
            bike2.setBrand("Bike 2");
            bike2.setRegistrationNo(4587545L);
            bike2.setIsAvailable(false);
            bikeRepository.save(bike2);

            Reservation reservation = new Reservation();
            reservation.setBikes(new ArrayList<>());
            reservation.setStartDate(LocalDate.of(2023, 7, 1));
            reservation.setEndDate(LocalDate.of(2023, 7, 3));
            reservationRepository.save(reservation);

            LocalDate startDate = LocalDate.of(2023, 7, 1);
            LocalDate endDate = LocalDate.of(2023, 7, 3);
            int bikeQuantity = 2;

            mockMvc.perform(MockMvcRequestBuilders.get("/bikes/checkAvailability")
                            .param("startDate", startDate.toString())
                            .param("endDate", endDate.toString())
                            .param("bikeQuantity", String.valueOf(bikeQuantity))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Bikes are available."));
        }
    }
