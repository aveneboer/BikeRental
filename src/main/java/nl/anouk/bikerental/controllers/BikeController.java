
package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.services.BikeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/bikes")
public class BikeController {
    private final BikeService bikeService;
    private final BikeRepository bikeRepository;

    public BikeController(BikeService bikeService, BikeRepository bikeRepository) {
        this.bikeService = bikeService;
        this.bikeRepository = bikeRepository;
    }


    @GetMapping("/checkAvailability")
    @Transactional
    public ResponseEntity<String> checkAvailability(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                    @RequestParam("bikeQuantity") int bikeQuantity) {
        if (startDate == null || endDate == null) {
            return ResponseEntity.badRequest().body("Start date and end date cannot be empty.");
        }

            boolean areBikesAvailable = bikeService.areBikesAvailable(startDate, endDate, bikeQuantity);

        if (areBikesAvailable) {
            return ResponseEntity.ok("Bikes are available.");
        } else {
            return ResponseEntity.ok("Bikes are not available.");
        }
    }


    @GetMapping("/all")
    @Transactional
    public List<BikeDto> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @GetMapping("/{id}")
    @Transactional
    public Bike getBikeById(@PathVariable Long id) {
        return bikeService.getBikeById(id);
    }


    @PostMapping("/add")
    @Transactional
    public ResponseEntity<Object> addBike(@Valid @RequestBody BikeInputDto bikeInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField() + ": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            BikeDto createdBike = bikeService.addBike(bikeInputDto);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + createdBike.getId()).toUriString());

            return ResponseEntity.created(uri).body(createdBike);
        }
    }

    @PatchMapping("/{id}")
    @Transactional
    public BikeDto updateBike(@PathVariable Long id, @RequestBody BikeInputDto updatedBikeInputDto) {
        return bikeService.partialUpdateBike(id, updatedBikeInputDto);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
        return ResponseEntity.noContent().build();
    }


}
