package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.services.ReservationLineService;
import nl.anouk.bikerental.services.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReservationLineControllerTest {

    @Mock
    private ReservationLineService reservationLineService;
    @Mock
    private ReservationService reservationService;

    private ReservationLineController reservationLineController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reservationLineController = new ReservationLineController(reservationLineService, reservationService);
    }

    @Test
    public void testGetReservationLine_ShouldReturnReservationLineDto() {
        // Arrange
        Long reservationLineId = 1L;
        ReservationLineDto expectedDto = createReservationLineDto(reservationLineId);
        when(reservationLineService.getReservationLineById(reservationLineId)).thenReturn(expectedDto);

        // Act
        ResponseEntity<ReservationLineDto> response = reservationLineController.getReservationLine(reservationLineId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedDto, response.getBody());
        verify(reservationLineService, times(1)).getReservationLineById(reservationLineId);
    }

    @Test
    public void testCreateReservationLine_ShouldReturnCreatedReservationLine() {
        // Arrange
        Long reservationId = 1L;
        ReservationLine expectedReservationLine = new ReservationLine();
        when(reservationLineService.createReservationLine(reservationId)).thenReturn(expectedReservationLine);

        // Act
        ResponseEntity<ReservationLine> response = reservationLineController.createReservationLine(reservationId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedReservationLine, response.getBody());
        verify(reservationLineService, times(1)).createReservationLine(reservationId);
    }

    @Test
    public void testCreateReservationLine_ShouldReturnBadRequestOnException() {
        // Arrange
        Long reservationId = 1L;
        when(reservationLineService.createReservationLine(reservationId)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ReservationLine> response = reservationLineController.createReservationLine(reservationId);

        // Assert
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(reservationLineService, times(1)).createReservationLine(reservationId);
    }

    @Test
    public void testGetAllReservationLines_ShouldReturnListOfReservationLineDto() {
        // Arrange
        List<ReservationLineDto> expectedDtos = createReservationLineDtoList();
        when(reservationLineService.getAllReservationLines()).thenReturn(expectedDtos);

        // Act
        ResponseEntity<List<ReservationLineDto>> response = reservationLineController.getAllReservationLines();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(expectedDtos, response.getBody());
        verify(reservationLineService, times(1)).getAllReservationLines();
    }


    private ReservationLineDto createReservationLineDto(Long reservationLineId) {
        ReservationLineDto dto = new ReservationLineDto();
        dto.setReservationLineId(reservationLineId);
        dto.setDateOrdered(LocalDateTime.now());
        dto.setConfirmation("CONFIRMED");
        dto.setDuration(7);
        dto.setTotalPrice(100.0);
        dto.setReservation(new ReservationDto());
        return dto;
    }

    private List<ReservationLineDto> createReservationLineDtoList() {
        List<ReservationLineDto> dtos = new ArrayList<>();
        dtos.add(createReservationLineDto(1L));
        dtos.add(createReservationLineDto(2L));
        return dtos;
    }
}