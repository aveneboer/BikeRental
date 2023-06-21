
package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.services.BikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping("/all")
    public List<BikeDto> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @GetMapping("/{id}")
    public Bike getBikeById(@PathVariable Long id) {
        return bikeService.getBikeById(id);
    }

    @GetMapping("/available_bikes")
    public ResponseEntity<List<BikeDto>> getAvailableBikes(@RequestParam LocalDate startDate,
                                                           @RequestParam LocalDate endDate,
                                                           @RequestParam int requiredQuantity) {
        List<BikeDto> availableBikes = bikeService.getAvailableBikes(startDate, endDate, requiredQuantity);
        return ResponseEntity.ok(availableBikes);
    }

    @PostMapping("/add")
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
    public BikeDto updateBike(@PathVariable Long id, @RequestBody BikeInputDto updatedBikeInputDto) {
        return bikeService.partialUpdateBike(id, updatedBikeInputDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
    }


}
