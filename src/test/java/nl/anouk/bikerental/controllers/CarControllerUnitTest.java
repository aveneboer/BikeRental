package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.inputs.CarInputDto;
import nl.anouk.bikerental.services.CarService;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import nl.anouk.bikerental.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(CarController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CarControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
        void checkAvailability () throws Exception {
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(7);

            when(carService.isCarAvailable(startDate, endDate)).thenReturn(true);

            mockMvc.perform(MockMvcRequestBuilders.get("/cars/checkAvailability")
                            .param("startDate", startDate.toString())
                            .param("endDate", endDate.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string("Car is available."));
    }

    @Test
    void getAllCars() throws Exception {
        List<CarDto> carDtos = new ArrayList<>();
        carDtos.add(new CarDto());
        carDtos.add(new CarDto());

        when(carService.getAllCars()).thenReturn(carDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/cars"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void addCar_WithValidInput_ReturnsCreated() throws Exception {
        CarInputDto carInputDto = new CarInputDto();
        carInputDto.setModel("Test Model");
        carInputDto.setPassenger(4);

        CarDto createdCarDto = new CarDto();
        createdCarDto.setId(1L);
        createdCarDto.setModel("Test Model");
        createdCarDto.setPassenger(4);

        when(carService.addCar(any(CarInputDto.class))).thenReturn(createdCarDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"model\":\"Test Model\",\"capacity\":4}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/cars/addCar/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Test Model"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passenger").value(4));
    }



    @Test
    void deleteCar_WithValidId_ReturnsNoContent() throws Exception {
        Long carId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/cars/cars/{id}", carId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(carService).deleteCar(carId);
    }


}