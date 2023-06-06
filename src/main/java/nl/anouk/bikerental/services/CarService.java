package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.dtos.CarInputDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Car;
import nl.anouk.bikerental.repositories.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDto> getAllCars() {
        List<Car> carList = carRepository.findAll();
        return transferCarListToDtoList(carList);
    }

    public List<CarDto> getAllCarsByModel(String model) {
        List<Car> carList = carRepository.findAllCarsByModelEqualsIgnoreCase(model);
        return transferCarListToDtoList(carList);
    }

    public List<CarDto> transferCarListToDtoList(List<Car> cars) {
        List<CarDto> carDtoList = new ArrayList<>();

        for (Car car : cars) {
            CarDto dto = transferToDto(car);
            carDtoList.add(dto);
        }

        return carDtoList;
    }

    public CarDto addCar(CarInputDto dto) {
        Car car = transferToCar(dto);
        carRepository.save(car);

        return transferToDto(car);
    }

    public CarDto getCarByPassenger(int passenger) {
        List<Car> cars = carRepository.findAllByPassengerGreaterThanEqual(passenger);
        if (!cars.isEmpty()) {
            Car car = cars.get(0); // Selecteer de eerste auto die aan de criteria voldoet
            return transferToDto(car);
        } else {
            throw new NoSuchElementException("No cars found for the given capacity");
        }
    }

    public CarDto updateCar(Long id, CarInputDto inputDto) {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();

            // Update de velden alleen als ze zijn opgegeven in de inputDto
            if (inputDto.getModel() != null) {
                existingCar.setModel(inputDto.getModel());
            }
            if (inputDto.getPassenger() != 0) {
                existingCar.setPassenger(inputDto.getPassenger());
            }
            if (inputDto.getDayPrice() != null) {
                existingCar.setDayPrice(inputDto.getDayPrice());
            }

            carRepository.save(existingCar);

            return transferToDto(existingCar);
        } else {
            throw new NoSuchElementException("Car not found");
        }
    }


    public void deleteCar(@RequestBody Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Car not found");
        }
    }

    public Car transferToCar(CarInputDto dto) {
        var car = new Car();
        car.setModel(dto.getModel());
        car.setPassenger(dto.getPassenger());
        car.setDayPrice(dto.getDayPrice());

        return car;
    }

    public CarDto transferToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setModel(car.getModel());
        dto.setPassenger(car.getPassenger());
        dto.setDayPrice(car.getDayPrice());

        return dto;
    }

}


