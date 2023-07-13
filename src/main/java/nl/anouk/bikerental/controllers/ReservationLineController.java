

package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.services.ReservationLineService;
import nl.anouk.bikerental.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
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

        @PostMapping("/reservation-line")
        public ResponseEntity<ReservationLine> createReservationLine(@RequestParam Long reservationId) {
            try {
                ReservationLine reservationLine = reservationLineService.createReservationLine(reservationId);
                return ResponseEntity.ok(reservationLine);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }



        @GetMapping("/reservationlines")
        public ResponseEntity<List<ReservationLineDto>> getAllReservationLines() {
            List<ReservationLineDto> dtos = reservationLineService.getAllReservationLines();
            return ResponseEntity.ok().body(dtos);
        }
    }


