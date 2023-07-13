package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.ReservationRepository;
import nl.anouk.bikerental.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class ReservationControllerTest {
    @Mock
    private ReservationService reservationService;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testGetAllReservations_ReturnsListOfReservations() {
        // Arrange
        List<ReservationDto> reservationDtos = new ArrayList<>();
        reservationDtos.add(new ReservationDto());
        reservationDtos.add(new ReservationDto());
        given(reservationService.getAllReservations()).willReturn(reservationDtos);

        // Act
        ResponseEntity<List<ReservationDto>> response = reservationController.getAllReservations();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationDtos, response.getBody());
        verify(reservationService).getAllReservations();
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testGetReservation_ValidId_ReturnsReservation() {
        // Arrange
        Long id = 1L;
        ReservationDto reservationDto = new ReservationDto();
        when(reservationService.getReservationById(id)).thenReturn(reservationDto);

        // Act
        ResponseEntity<ReservationDto> response = reservationController.getReservation(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationDto, response.getBody());
        verify(reservationService).getReservationById(id);
    }
    @Test
    @WithMockUser(username="testuser", roles="USER")
    void testCreateReservation_ValidationErrors_ReturnsBadRequest() {
        // Arrange
        ReservationInputDto reservationInputDto = new ReservationInputDto();

        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasFieldErrors()).willReturn(true);
        given(bindingResult.getFieldErrors()).willReturn(Arrays.asList(
                new FieldError("reservationInputDto", "startDate", "Start date is required."),
                new FieldError("reservationInputDto", "endDate", "End date is required.")
        ));

        // Act
        ResponseEntity<Object> response = reservationController.createReservation(reservationInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("startDate: Start date is required.\nendDate: End date is required.\n", response.getBody());
        verify(reservationService, never()).createReservation(any(ReservationInputDto.class));
    }


    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testDeleteReservation_ValidId_ReturnsNoContent() {
        // Arrange
        Long id = 1L;
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        // Act
        ResponseEntity<Object> response = reservationController.deleteReservation(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(reservationRepository).findById(id);
        verify(reservationRepository).delete(reservation);
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testUpdateReservation_ValidIdAndInput_ReturnsUpdatedReservation() {
        // Arrange
        Long id = 1L;
        ReservationInputDto newReservation = new ReservationInputDto();
        ReservationDto dto = new ReservationDto();
        when(reservationService.updateReservation(id, newReservation)).thenReturn(dto);

        // Act
        ResponseEntity<Object> response = reservationController.updateReservation(id, newReservation);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(reservationService).updateReservation(id, newReservation);
    }
}