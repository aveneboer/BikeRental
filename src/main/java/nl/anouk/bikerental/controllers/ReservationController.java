package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.dtos.ReservationInputDto;
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

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable("id") Long id) {
        ReservationDto reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok().body(reservation);
    }

    @PostMapping("/reservations")
    public ResponseEntity<Object> createReservation(@Valid @RequestBody ReservationInputDto reservationInputDto) {
        ReservationDto dto = reservationService.createReservation(reservationInputDto);
        return ResponseEntity.created(null).body(dto);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservations/{id}")
    public ResponseEntity<Object> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationInputDto newReservation) {
        ReservationDto dto = reservationService.updateReservation(id, newReservation);
        return ResponseEntity.ok().body(dto);
    }}


