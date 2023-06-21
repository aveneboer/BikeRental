package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.exceptions.BikeNotAvailableException;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> dtos = reservationService.getAllReservations();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable("id") Long id) {
        ReservationDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok().body(reservation);
    }

    @PostMapping("/reservation")
    public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationInputDto reservationInputDto) {
        ReservationDto dto = reservationService.createReservation(reservationInputDto);
        return ResponseEntity.created(null).body(dto);
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationInputDto inputDto) {
        try {
            ReservationInputDto reservationInputDto = new ReservationInputDto();
            reservationInputDto.setStartDate(inputDto.getStartDate());
            reservationInputDto.setEndDate(inputDto.getEndDate());
            reservationInputDto.setCustomerId(inputDto.getCustomerId());
            reservationInputDto.setType(inputDto.getType());
            reservationInputDto.setBikeId(inputDto.getBikeId());
            reservationInputDto.setReservationLine(inputDto.getReservationLine());

            Reservation reservation = DtoMapper.mapReservationInputDtoToEntity(reservationInputDto);
            reservation = reservationService.createReservation(reservation);
            ReservationDto reservationDto = DtoMapper.mapReservationToDto(reservation);
            return ResponseEntity.ok(reservationDto);
        } catch (BikeNotAvailableException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<Object> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationInputDto newReservation) {
        ReservationDto dto = reservationService.updateReservation(id, newReservation);
        return ResponseEntity.ok().body(dto);
    }}


