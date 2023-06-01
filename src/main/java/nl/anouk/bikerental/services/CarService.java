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
    public CarDto getCarByCapacity(int passenger) {

        if (carRepository.findAllByCapacity(passenger).isPresent()) {
            Car car = carRepository.findAllByCapacity(passenger).get();
            CarDto dto = transferToDto(car);

            return transferToDto(car);
        } else {
            throw new RecordNotFoundException("No car was found");
        }
    }

    public CarDto updateCar(Long id, CarInputDto inputDto) {
        if (carRepository.findAllById(id).isPresent()) {

            Car car = carRepository.findAllById(id).get();

            Car car1 = transferToCar(inputDto);
            car1.setVehicleId(car.getVehicleId());

            carRepository.save(car1);

            return transferToDto(car1);

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

        return car;
    }

    public CarDto transferToDto(Car car) {
        CarDto dto = new CarDto();

        dto.setModel(car.getModel());
        dto.setPassenger(car.getPassenger());

        return dto;
    }

}
