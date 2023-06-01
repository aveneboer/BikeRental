package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars(@RequestParam(value = "model", required = false) Optional<String> model) {

        List<CarDto> dtos;

        if (model.isEmpty()) {
            dtos = carService.getAllCars();

        } else {

            dtos = carService.getAllCarsByModel(model.get());
        }
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/cars/{capacity}")
    public ResponseEntity<CarDto> getCar(@PathVariable("capacity")int passenger) {

        CarDto car = carService.getCarByCapacity(passenger);

        return ResponseEntity.ok().body(car);

    }

    @PostMapping("/cars")
    public ResponseEntity<Object> addCar(@Valid @RequestBody CarInputDto carInputDto) {

        CarDto dto = carService.addCar(carInputDto);

        return ResponseEntity.created(null).body(dto);
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/cars/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable Long id, @Valid @RequestBody CarInputDto newCar) {
        CarDto dto = carService.updateCar(id, newCar);

        return ResponseEntity.ok().body(dto);

    }

}
