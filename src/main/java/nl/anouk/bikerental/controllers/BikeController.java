
package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.services.BikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }


    ////deze getmapping controleren, klopt nog niet

    @GetMapping("/available")
    public List<BikeDto> getAvailableBikes() {
        return bikeService.getAvailableBikes();
    }

    @GetMapping("/all")
    public List<BikeDto> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @GetMapping("/{id}")
    public Bike getBikeById(@PathVariable Long id) {
        return bikeService.getBikeById(id);
    }

    @PostMapping("/add")
    public BikeDto addBike(@RequestBody BikeInputDto inputDto) {
        return bikeService.addBike(inputDto);
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
