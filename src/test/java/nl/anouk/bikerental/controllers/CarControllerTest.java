package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.inputs.CarInputDto;
import nl.anouk.bikerental.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class CarControllerTest {
    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAvailability_CarAvailable_ReturnsOk() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.of(2023, 6, 10);
        boolean isCarAvailable = true;
        given(carService.isCarAvailable(startDate, endDate)).willReturn(isCarAvailable);

        // Act
        ResponseEntity<String> response = carController.checkAvailability(startDate, endDate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car is available.", response.getBody());
        verify(carService).isCarAvailable(startDate, endDate);
    }

    @Test
    void testCheckAvailability_CarNotAvailable_ReturnsOk() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.of(2023, 6, 10);
        boolean isCarAvailable = false;
        given(carService.isCarAvailable(startDate, endDate)).willReturn(isCarAvailable);

        // Act
        ResponseEntity<String> response = carController.checkAvailability(startDate, endDate);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car is not available.", response.getBody());
        verify(carService).isCarAvailable(startDate, endDate);
    }

    @Test
    void testGetAllCars_NoModel_ReturnsOkWithCars() {
        // Arrange
        List<CarDto> cars = Arrays.asList(
                new CarDto(1L, "Nissan", 4, true, 3),
                new CarDto(2L, "Toyota",3, false, 1)
        );
        given(carService.getAllCars()).willReturn(cars);

        // Act
        ResponseEntity<List<CarDto>> response = carController.getAllCars(Optional.empty());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
        verify(carService).getAllCars();
    }

    @Test
    void testGetAllCars_WithModel_ReturnsOkWithFilteredCars() {
        // Arrange
        String model = "Sedan";
        List<CarDto> cars = Arrays.asList(
                new CarDto(1L, "Nissan", 4, true, 3),
                new CarDto(2L, "Toyota",3, false, 1)
        );
        given(carService.getAllCarsByModel(model)).willReturn(cars);

        // Act
        ResponseEntity<List<CarDto>> response = carController.getAllCars(Optional.of(model));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
        verify(carService).getAllCarsByModel(model);
    }

    @Test
    void testGetCar_ReturnsOkWithCar() {
        // Arrange
        int passenger = 4;
        CarDto car = new CarDto(1L, "Nissan", 4, true, 3);
        given(carService.getCarByPassenger(passenger)).willReturn(car);

        // Act
        ResponseEntity<CarDto> response = carController.getCar(passenger);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(car, response.getBody());
        verify(carService).getCarByPassenger(passenger);
    }

    @Test
    void testAddCar_ValidCarInputDto_ReturnsCreatedCarDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        CarInputDto carInputDto = new CarInputDto();
        carInputDto.setModel("Model 1");
        carInputDto.setPassenger(3);
        carInputDto.setIsAvailable(true);


        CarDto createdCarDto = new CarDto();
        createdCarDto.setId(1L);
        createdCarDto.setModel("Model 1");
        createdCarDto.setPassenger(3);
        createdCarDto.setIsAvailable(true);

        when(carService.addCar(any(CarInputDto.class))).thenReturn(createdCarDto);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<Object> response = carController.addCar(carInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCarDto, response.getBody());
        verify(carService, times(1)).addCar(carInputDto);

        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    void testAddCar_ValidationErrors_ReturnsBadRequest() {
        // Arrange
        CarInputDto carInputDto = new CarInputDto();
        carInputDto.setModel("");
        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasFieldErrors()).willReturn(true);
        given(bindingResult.getFieldErrors()).willReturn(Arrays.asList(
                new FieldError("carInputDto", "model", "Model is required.")
        ));

        // Act
        ResponseEntity<Object> response = carController.addCar(carInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("model: Model is required.\n", response.getBody());
        verify(carService, never()).addCar(any(CarInputDto.class));
    }

    @Test
    void testDeleteCar_ReturnsNoContent() {
        // Arrange
        Long carId = 1L;

        // Act
        ResponseEntity<Object> response = carController.deleteCar(carId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(carService).deleteCar(carId);
    }

    @Test
    void testUpdateCar_ReturnsUpdatedCar() {
        // Arrange
        Long carId = 1L;
        CarInputDto updatedCarInputDto = new CarInputDto();
        updatedCarInputDto.setModel("Updated Car");
        CarDto updatedCarDto = new CarDto(carId, "Updated Car", 4 , false, 2 );
        given(carService.partialUpdateCar(carId, updatedCarInputDto)).willReturn(updatedCarDto);

        // Act
        CarDto result = carController.updateCar(carId, updatedCarInputDto);

        // Assert
        assertEquals(updatedCarDto, result);
        verify(carService).partialUpdateCar(carId, updatedCarInputDto);
    }
}
