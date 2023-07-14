package nl.anouk.bikerental.services;


import nl.anouk.bikerental.repositories.CarRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CarServiceIntegrationTest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testContextLoads() {
        assertNotNull(carService);
    }
}

   /* @Test
    public void testIsCarAvailable_WhenNoOverlap_ReturnsTrue() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 7, 15);
        LocalDate endDate = LocalDate.of(2023, 7, 20);
        Long carId = 1L;

        // Act
        Car car = carRepository.findById(carId).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        Hibernate.initialize(car.getReservations());
        boolean isAvailable = carService.isCarAvailable(startDate, endDate);

        // Assert
        assertTrue(isAvailable);
    }


    @Test
    public void testIsCarAvailable_WhenOverlap_ReturnsFalse() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 7, 15);
        LocalDate endDate = LocalDate.of(2023, 7, 20);

        // Create a reservation that overlaps with the test period
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2023, 7, 18));
        reservation.setEndDate(LocalDate.of(2023, 7, 22));
        reservationRepository.save(reservation);

        // Assign the reservation to a car
        Car car = new Car();
        car.getReservations().add(reservation);
        carRepository.save(car);

        // Act
        boolean isAvailable = carService.isCarAvailable(startDate, endDate);

        // Assert
        assertFalse(isAvailable);
    }*/


