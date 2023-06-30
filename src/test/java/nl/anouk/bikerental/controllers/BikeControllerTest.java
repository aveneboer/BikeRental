package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.services.BikeService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class BikeControllerTest {
    @Mock
    private BikeService bikeService;

    @Mock
    private BikeRepository bikeRepository;


    @InjectMocks
    private BikeController bikeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckAvailability_BikesAvailable_ReturnsOk() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.of(2023, 6, 10);
        int bikeQuantity = 3;
        boolean areBikesAvailable = true;
        given(bikeService.areBikesAvailable(startDate, endDate, bikeQuantity)).willReturn(areBikesAvailable);

        // Act
        ResponseEntity<String> response = bikeController.checkAvailability(startDate, endDate, bikeQuantity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bikes are available.", response.getBody());
        verify(bikeService).areBikesAvailable(startDate, endDate, bikeQuantity);
    }

    @Test
    void testCheckAvailability_BikesNotAvailable_ReturnsOk() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 6, 1);
        LocalDate endDate = LocalDate.of(2023, 6, 10);
        int bikeQuantity = 3;
        boolean areBikesAvailable = false;
        given(bikeService.areBikesAvailable(startDate, endDate, bikeQuantity)).willReturn(areBikesAvailable);

        // Act
        ResponseEntity<String> response = bikeController.checkAvailability(startDate, endDate, bikeQuantity);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bikes are not available.", response.getBody());
        verify(bikeService).areBikesAvailable(startDate, endDate, bikeQuantity);
    }

    @Test
    void testGetAllBikes_ReturnsListOfBikeDtos() {
        // Arrange
        List<BikeDto> expectedBikeDtos = Arrays.asList(
                new BikeDto(1L, "Bike 1", 123456L, true),
                new BikeDto(2L, "Bike 2", 789012L, false)
        );
        given(bikeService.getAllBikes()).willReturn(expectedBikeDtos);

        // Act
        List<BikeDto> bikeDtos = bikeController.getAllBikes();

        // Assert
        assertEquals(expectedBikeDtos, bikeDtos);
        verify(bikeService).getAllBikes();
    }

    @Test
    void testGetBikeById_ReturnsBikeDto() {
        // Arrange
        Long bikeId = 1L;
        Bike expectedBike = new Bike(1L, "Bike 1", 123456L, true, new ArrayList<>());
        given(bikeService.getBikeById(bikeId)).willReturn(expectedBike);

        // Act
        Bike resultBike = bikeController.getBikeById(bikeId);

        // Assert
        assertEquals(expectedBike, resultBike);
        verify(bikeService).getBikeById(bikeId);
    }
    @Test
    void testAddBike_ValidationErrors_ReturnsBadRequest() {
        // Arrange
        BikeInputDto bikeInputDto = new BikeInputDto();
        bikeInputDto.setBrand("");
        bikeInputDto.setRegistrationNo(123456L);
        bikeInputDto.setIsAvailable(true);
        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasFieldErrors()).willReturn(true);
        given(bindingResult.getFieldErrors()).willReturn(Arrays.asList(
                new FieldError("bikeInputDto", "brand", "Brand is required.")
        ));

        // Act
        ResponseEntity<Object> response = bikeController.addBike(bikeInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("brand: Brand is required.\n", response.getBody());
        verify(bikeService, never()).addBike(any(BikeInputDto.class));
    }
    @Test
    void testAddBike_ValidBikeInputDto_ReturnsCreatedBikeDto() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();

        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);

        RequestContextHolder.setRequestAttributes(requestAttributes);

        BikeInputDto bikeInputDto = new BikeInputDto();
        bikeInputDto.setBrand("Brand 1");
        bikeInputDto.setRegistrationNo(123456L);
        bikeInputDto.setIsAvailable(true);

        BikeDto createdBikeDto = new BikeDto();
        createdBikeDto.setId(1L);
        createdBikeDto.setBrand("Brand 1");
        createdBikeDto.setRegistrationNo(123456L);
        createdBikeDto.setIsAvailable(true);

        when(bikeService.addBike(any(BikeInputDto.class))).thenReturn(createdBikeDto);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<Object> response = bikeController.addBike(bikeInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdBikeDto, response.getBody());
        verify(bikeService, times(1)).addBike(bikeInputDto);

        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    void testUpdateBike_ReturnsUpdatedBikeDto() {
        // Arrange
        Long bikeId = 1L;
        BikeInputDto updatedBikeInputDto = new BikeInputDto();
        updatedBikeInputDto.setBrand("Updated Bike");
        updatedBikeInputDto.setRegistrationNo(654321L);
        updatedBikeInputDto.setIsAvailable(false);
        BikeDto updatedBikeDto = new BikeDto(bikeId, "Updated Bike", 654321L, false);
        when(bikeService.partialUpdateBike(bikeId, updatedBikeInputDto)).thenReturn(updatedBikeDto);

        // Act
        BikeDto response = bikeController.updateBike(bikeId, updatedBikeInputDto);

        // Assert
        assertEquals(updatedBikeDto, response);
        verify(bikeService).partialUpdateBike(bikeId, updatedBikeInputDto);
    }

    @Test
    void testDeleteBike_ReturnsNoContent() {
        // Arrange
        Long bikeId = 1L;

        // Act
        bikeController.deleteBike(bikeId);

        // Assert
        verify(bikeService).deleteBike(bikeId);
    }
}