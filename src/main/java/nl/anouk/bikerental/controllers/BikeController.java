package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.services.BikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    @GetMapping("/bikes")
    public ResponseEntity<List<BikeDto>> getAllBikes(@RequestParam(value = "brand", required = false) Optional<String> brand) {

        List<BikeDto> dtos;

        if (brand.isEmpty()) {
            dtos = bikeService.getAllBikes();

        } else {

            dtos = bikeService.getAllBikesByBrand(brand.get());
        }
        return ResponseEntity.ok().body(dtos);
    }


    @GetMapping("/bikes/{id}")
    public ResponseEntity<BikeDto> getBike(@PathVariable("id")Long id) {

        BikeDto bike = bikeService.getBikeById(id);

        return ResponseEntity.ok().body(bike);

    }

    @PostMapping("/bikes")
    public ResponseEntity<Object> addBike(@Valid @RequestBody BikeInputDto bikeInputDto) {

        BikeDto dto = bikeService.addBike(bikeInputDto);

        return ResponseEntity.created(URI.create("/bikes/" + dto.getId())).body(dto);
    }

    @DeleteMapping("/bikes/{id}")
    public ResponseEntity<Object> deleteBike(@PathVariable Long id) {
        bikeService.deleteBike(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/bikes/{id}")
    public ResponseEntity<Object> updateBike(@PathVariable Long id, @Valid @RequestBody BikeInputDto newBike) {
        BikeDto dto = bikeService.updateBike(id, newBike);

        return ResponseEntity.ok().body(dto);

    }


}

