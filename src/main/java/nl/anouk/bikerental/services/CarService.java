
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.inputs.CarInputDto;
import nl.anouk.bikerental.models.Car;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.repositories.CarRepository;
import org.springframework.stereotype.Service;

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
        return DtoMapper.mapCarListToDtoList(carList);
    }

    public List<CarDto> getAllCarsByModel(String model) {
        List<Car> carList = carRepository.findAllCarsByModelEqualsIgnoreCase(model);
        return DtoMapper.mapCarListToDtoList(carList);
    }


    public CarDto addCar(CarInputDto dto) {
        Car car = DtoMapper.mapCarInputDtoToEntity(dto);
        carRepository.save(car);

        return DtoMapper.mapCarToDto(car);
    }

    public CarDto getCarByPassenger(int passenger) {
        List<Car> cars = carRepository.findAllByPassengerGreaterThanEqual(passenger);
        if (!cars.isEmpty()) {
            Car car = cars.get(0);
            return DtoMapper.mapCarToDto(car);
        } else {
            throw new NoSuchElementException("No cars found for the given capacity");
        }
    }

    public CarDto partialUpdateCar(Long id, CarInputDto inputDto) {
        Optional<Car> optionalCar = carRepository.findById(id);

        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();

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

            return DtoMapper.mapCarToDto(existingCar);
        } else {
            throw new NoSuchElementException("Car not found");
        }
    }

    public void deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Car not found");
        }
    }
}

