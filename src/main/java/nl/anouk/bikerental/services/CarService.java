
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CarDto;
import nl.anouk.bikerental.inputs.CarInputDto;
import nl.anouk.bikerental.models.Car;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.CarRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public CarService(CarRepository carRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }


    public boolean isCarAvailable(LocalDate startDate, LocalDate endDate) {
        List<Car> allCars = carRepository.findAll();

        for (Car car : allCars) {
            for (Reservation reservation : car.getReservations()) {
                if (endDate != null && reservation.getStartDate() != null && reservation.getEndDate() != null) {
                    if (endDate.isAfter(reservation.getStartDate()) && startDate.isBefore(reservation.getEndDate())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public List<Long> getAvailableCarIds(LocalDate startDate, LocalDate endDate) {
        List<Car> availableCars = carRepository.findAllByIsAvailable(true);
        if (availableCars == null || availableCars.isEmpty()) {
            throw new IllegalArgumentException("No cars are available.");
        }

        List<Long> availableCarIds = new ArrayList<>();

        for (Car car : availableCars) {
            boolean isAvailable = true;
            for (Reservation reservation : car.getReservations()) {
                if (endDate != null && reservation.getStartDate() != null && reservation.getEndDate() != null) {
                    if (endDate.isAfter(reservation.getStartDate()) && startDate.isBefore(reservation.getEndDate())) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) {
                availableCarIds.add(car.getId());
            }
        }

        return availableCarIds;
    }

    public Car getCarById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + carId));
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
            if (inputDto.getQuantity() != 0) {
                existingCar.setQuantity(inputDto.getQuantity());
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

