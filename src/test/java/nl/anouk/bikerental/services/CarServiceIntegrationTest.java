package nl.anouk.bikerental.services;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nl.anouk.bikerental.models.Car;
import nl.anouk.bikerental.repositories.CarRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@Transactional
public class CarServiceIntegrationTest {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {

        Car car = carRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Car not found"));
        Hibernate.initialize(car.getReservations());
        entityManager.detach(car);
    }

    @Test
    public void testIsCarAvailable_WhenNoOverlap_ReturnsTrue() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 7, 15);
        LocalDate endDate = LocalDate.of(2023, 7, 20);

        // Act
        boolean isAvailable = carService.isCarAvailable(startDate, endDate);

        // Assert
        assertTrue(isAvailable);
    }

}


