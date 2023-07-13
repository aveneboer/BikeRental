package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.models.Car;
import nl.anouk.bikerental.repositories.CarRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CarServiceIntegrationTest {
    @Autowired
    private CarService carService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    public void testGetAllCars() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Toyota Highlander"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].passenger").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value("Toyota RAV4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].passenger").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2));
    }


    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    public void testAddCar() throws Exception {
        String carJson = "{ \"model\": \"Hatchback\", \"passenger\": 4, \"isAvailable\": true, \"quantity\": 2 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Hatchback"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passenger").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2));
    }

    @Test
    public void testIsCarAvailable_ReturnsTrue() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // Act
        boolean isAvailable = carService.isCarAvailable(startDate, endDate);

        // Assert
        assertTrue(isAvailable);
    }

    @Test
    public void testGetAvailableCarIds_ReturnsAvailableCarIds() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // Act
        List<Long> availableCarIds = carService.getAvailableCarIds(startDate, endDate);

        // Assert
        assertFalse(availableCarIds.isEmpty());
    }
    @Test
    public void testGetCarById_ReturnsCar() {
        // Arrange
        Car car = new Car();
        car.setModel("Test Model");
        car.setQuantity(1);
        car.setPassenger(4);
        car.setIsAvailable(true);
        car = carRepository.save(car);

        // Act
        Car retrievedCar = carService.getCarById(car.getId());

        // Assert
        assertNotNull(retrievedCar);
        assertEquals(car.getId(), retrievedCar.getId());
    }
    @Test
    public void testGetAllCars_ReturnsAllCars() {
        // Arrange
        Car car1 = new Car();
        car1.setModel("Model 1");
        car1.setQuantity(1);
        car1.setPassenger(4);
        car1.setIsAvailable(true);
        carRepository.save(car1);

        Car car2 = new Car();
        car2.setModel("Model 2");
        car2.setQuantity(1);
        car2.setPassenger(4);
        car2.setIsAvailable(true);
        carRepository.save(car2);

        // Act
        List<CarDto> carDtos = carService.getAllCars();

        // Assert
        assertEquals(2, carDtos.size());
    }

}
