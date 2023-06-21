
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.exceptions.BikeNotFoundException;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.BikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final DtoMapper dtoMapper;

    public BikeService(BikeRepository bikeRepository, DtoMapper dtoMapper) {
        this.bikeRepository = bikeRepository;
        this.dtoMapper = dtoMapper;
    }
/*
        public boolean checkBikeAvailability(Long bikeId, LocalDate startDate, LocalDate endDate) {
            Bike bike = bikeRepository.findById(bikeId)
                    .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));

            return isBikeAvailable(bike, startDate, endDate);
        }*/

/*    public List<BikeDto> getAvailableBikes(LocalDate startDate, LocalDate endDate, int requiredQuantity) {
        List<Bike> bikes = bikeRepository.findAll();
        List<BikeDto> availableBikes = new ArrayList<>();

        for (Bike bike : bikes) {
            boolean isAvailable = isBikeAvailable(bike, startDate, endDate, requiredQuantity);
            if (isAvailable) {
                BikeDto bikeDto = dtoMapper.mapBikeToDto(bike);
                availableBikes.add(bikeDto);
            }
        }

        return availableBikes;
    }*/

    public boolean isBikeAvailable(LocalDate startDate, LocalDate endDate, int requiredQuantity) {
        List<Bike> bikes = bikeRepository.findAll(); // Haal alle fietsen op

        for (Bike bike : bikes) {
            int availableQuantity = bike.getQuantity();

            for (Reservation reservation : bike.getReservations()) {
                if (startDate.isBefore(reservation.getEndDate()) && endDate.isAfter(reservation.getStartDate())) {
                    availableQuantity -= reservation.getBikeQuantity();
                    if (availableQuantity < requiredQuantity) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    public void updateBikeAvailability(Long bikeId, boolean isAvailable) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new BikeNotFoundException("Fiets niet gevonden met ID: " + bikeId));
        bike.setIsAvailable(isAvailable);
        bikeRepository.save(bike);
    }
/*

    public void rentBike(Long bikeId) {
            Bike bike = bikeRepository.findById(bikeId)
                    .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));

            if (!bike.isAvailable()) {
                throw new BikeNotAvailableException("De fiets is niet beschikbaar.");
            }

            bike.setIsAvailable(false);
            bikeRepository.save(bike);
        }
*/


/*    public BikeDto getBikeById(Long id) {
        Optional<Bike> optionalBike = bikeRepository.findById(id);

        if (optionalBike.isPresent()) {
            Bike bike = optionalBike.get();
            return DtoMapper.mapBikeToDto(bike);
        } else {
            throw new RecordNotFoundException("No bike was found");
        }
    }*/

    public List<BikeDto> getAvailableBikes(LocalDate startDate, LocalDate endDate, int requiredQuantity) {
        List<Bike> bikes = bikeRepository.findAll();
        List<BikeDto> availableBikes = new ArrayList<>();

        for (Bike bike : bikes) {
            boolean isAvailable = isBikeAvailable(startDate, endDate, requiredQuantity);
            if (isAvailable) {
                BikeDto bikeDto = dtoMapper.mapBikeToDto(bike);
                availableBikes.add(bikeDto);
            }
        }

        return availableBikes;
    }

    //standaard code
    public Bike getBikeById(Long bikeId) {
        return bikeRepository.findById(bikeId)
                .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));
    }

    public void deleteBike(Long bikeId) {
        if (bikeRepository.existsById(bikeId)) {
            bikeRepository.deleteById(bikeId);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }

    public List<BikeDto> getAllBikes() {
        List<Bike> bikeList = bikeRepository.findAll();
        return DtoMapper.mapBikeListToDtoList(bikeList);
    }

    public BikeDto addBike(BikeInputDto inputDto) {
        Bike bike = DtoMapper.mapBikeInputDtoToEntity(inputDto);
        bikeRepository.save(bike);

        return DtoMapper.mapBikeToDto(bike);
    }

    public BikeDto partialUpdateBike(Long id, BikeInputDto updatedBikeInputDto) {
        Optional<Bike> optionalBike = bikeRepository.findById(id);

        if (optionalBike.isPresent()) {
            Bike existingBike = optionalBike.get();

            if (updatedBikeInputDto.getBrand() != null) {
                existingBike.setBrand(updatedBikeInputDto.getBrand());
            }
            if (updatedBikeInputDto.getQuantity() != 0) {
                existingBike.setQuantity(updatedBikeInputDto.getQuantity());
            }
            if (updatedBikeInputDto.getRegistrationNo() != null) {
                existingBike.setRegistrationNo(updatedBikeInputDto.getRegistrationNo());
            }
            if (updatedBikeInputDto.getHourlyPrice() != null) {
                existingBike.setHourlyPrice(updatedBikeInputDto.getHourlyPrice());
            }

            bikeRepository.save(existingBike);

            return DtoMapper.mapBikeToDto(existingBike);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }


}
