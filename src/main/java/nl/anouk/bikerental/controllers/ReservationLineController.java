

package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.inputs.ReservationLineInputDto;
import nl.anouk.bikerental.services.ReservationLineService;
import nl.anouk.bikerental.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    @RestController
    public class ReservationLineController {
        private final ReservationLineService reservationLineService;
        private final ReservationService reservationService;

        public ReservationLineController(ReservationLineService reservationLineService, ReservationService reservationService) {
            this.reservationLineService = reservationLineService;
            this.reservationService = reservationService;
        }

        @GetMapping("reservationlines/{id}")
        public ResponseEntity<ReservationLineDto> getReservationLine(@PathVariable("id") Long id) {
            ReservationLineDto reservationLine = reservationLineService.getReservationLineById(id);
            return ResponseEntity.ok().body(reservationLine);
        }

        @PostMapping("/reservationline")
        public ResponseEntity<Object> createReservationLine(@Valid @RequestBody ReservationLineInputDto reservationLineInputDto, @RequestParam Long reservationId) {
            ReservationDto reservationDto = reservationService.getReservationById(reservationId);
            ReservationLineDto createdReservationLine = reservationLineService.createReservationLine(reservationDto, reservationLineInputDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReservationLine);
        }


        @GetMapping("/reservationlines")
        public ResponseEntity<List<ReservationLineDto>> getAllReservationLines() {
            List<ReservationLineDto> dtos = reservationLineService.getAllReservationLines();
            return ResponseEntity.ok().body(dtos);
        }
    }


