package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.repositories.ReservationLineRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReservationLineServiceTest {
    @InjectMocks
    private ReservationLineService reservationLineService;

    @Mock
    private ReservationLineRepository reservationLineRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        reservationLineService = new ReservationLineService(reservationLineRepository, reservationRepository);
    }

    @Test
    public void testGetReservationLineById() {
        // Arrange
        Long reservationLineId = 1L;
        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setReservationLineId(reservationLineId);
        Mockito.when(reservationLineRepository.findById(reservationLineId)).thenReturn(Optional.of(reservationLine));

        // Act
        ReservationLineDto result = reservationLineService.getReservationLineById(reservationLineId);

        // Assert
        Assertions.assertEquals(reservationLineId, result.getReservationLineId());
        Mockito.verify(reservationLineRepository, Mockito.times(1)).findById(reservationLineId);
    }

    @Test
    public void testGetReservationLineById_RecordNotFoundException() {
        // Arrange
        Long reservationLineId = 1L;
        Mockito.when(reservationLineRepository.findById(reservationLineId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            reservationLineService.getReservationLineById(reservationLineId);
        });
        Mockito.verify(reservationLineRepository, Mockito.times(1)).findById(reservationLineId);
    }

    @Test
    public void testGetAllReservationLines() {
        // Arrange
        List<ReservationLine> reservationLines = new ArrayList<>();
        reservationLines.add(new ReservationLine());
        Mockito.when(reservationLineRepository.findAll()).thenReturn(reservationLines);

        // Act
        List<ReservationLineDto> result = reservationLineService.getAllReservationLines();

        // Assert
        Assertions.assertEquals(reservationLines.size(), result.size());
        Mockito.verify(reservationLineRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testCreateReservationLine_BikeType() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setType("bike");
        reservation.setBikeQuantity(2);
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(5));
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        Mockito.when(reservationLineRepository.save(Mockito.any(ReservationLine.class))).thenReturn(new ReservationLine());

        // Act
        ReservationLine result = reservationLineService.createReservationLine(reservationId);

        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(reservationRepository, Mockito.times(1)).findById(reservationId);
        Mockito.verify(reservationLineRepository, Mockito.times(1)).save(Mockito.any(ReservationLine.class));
    }

    @Test
    public void testCreateReservationLine_CarType() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setType("car");
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(5));
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        Mockito.when(reservationLineRepository.save(Mockito.any(ReservationLine.class))).thenReturn(new ReservationLine());

        // Act
        ReservationLine result = reservationLineService.createReservationLine(reservationId);

        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(reservationRepository, Mockito.times(1)).findById(reservationId);
        Mockito.verify(reservationLineRepository, Mockito.times(1)).save(Mockito.any(ReservationLine.class));
    }

    @Test
    public void testCreateReservationLine_InvalidType() {
        // Arrange
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setReservationId(reservationId);
        reservation.setType("invalid");
        Mockito.when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> {
            reservationLineService.createReservationLine(reservationId);
        });
        Mockito.verify(reservationRepository, Mockito.times(1)).findById(reservationId);
        Mockito.verify(reservationLineRepository, Mockito.never()).save(Mockito.any(ReservationLine.class));
    }

}