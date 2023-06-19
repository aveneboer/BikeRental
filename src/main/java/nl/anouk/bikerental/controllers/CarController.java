
package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.inputs.CarInputDto;
import nl.anouk.bikerental.services.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/findCars/{capacity}")
    public ResponseEntity<CarDto> getCar(@PathVariable("capacity")int passenger) {

        CarDto car = carService.getCarByPassenger(passenger);

        return ResponseEntity.ok().body(car);

    }

    @PostMapping("/addCar")
    public ResponseEntity<Object> addCar(@Valid @RequestBody CarInputDto carInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField() + ": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            CarDto createdCar = carService.addCar(carInputDto);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + createdCar.getId()).toUriString());

            return ResponseEntity.created(uri).body(createdCar);
        }
    }


    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Object> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/cars/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable Long id, @Valid @RequestBody CarInputDto newCar){
            CarDto dto = carService.updateCar(id, newCar);

            return ResponseEntity.ok().body(dto);

        }
}

