
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.exceptions.BikeNotFoundException;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;
    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;

    public BikeService(BikeRepository bikeRepository, DtoMapper dtoMapper, ReservationRepository reservationRepository) {
        this.bikeRepository = bikeRepository;
        this.dtoMapper = dtoMapper;
        this.reservationRepository = reservationRepository;
    }


    public boolean areBikesAvailable(LocalDate startDate, LocalDate endDate, int bikeQuantity) {
        List<Bike> allBikes = bikeRepository.findAll();
        List<Bike> availableBikes = new ArrayList<>(allBikes);

        for (Bike bike : allBikes) {
            for (Reservation reservation : bike.getReservations()) {
                if (endDate != null && reservation.getStartDate() != null && reservation.getEndDate() != null) {
                    if (endDate.isAfter(reservation.getStartDate()) && startDate.isBefore(reservation.getEndDate())) {
                        availableBikes.remove(bike);
                        break;
                    }
                }
            }
        }

        return availableBikes.size() >= bikeQuantity;
    }

    public List<Long> getAvailableBikeIds(LocalDate startDate, LocalDate endDate, int bikeQuantity) {
        List<Long> availableBikeIds = new ArrayList<>();

        List<Bike> availableBikes = bikeRepository.findAllByIsAvailable(true);
        for (Bike bike : availableBikes) {
            boolean isAvailable = true;
            List<Reservation> reservations = reservationRepository.findActiveReservationsByBike(bike);
            for (Reservation reservation : reservations) {
                if (endDate != null && reservation.getStartDate() != null && reservation.getEndDate() != null) {
                    if (endDate.isAfter(reservation.getStartDate()) && startDate.isBefore(reservation.getEndDate())) {
                        isAvailable = false;
                        break;
                    }
                }
            }
            if (isAvailable) {
                availableBikeIds.add(bike.getId());
                if (availableBikeIds.size() == bikeQuantity) {
                    break;
                }
            }
        }

        return availableBikeIds;
    }




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
            if (updatedBikeInputDto.getRegistrationNo() != null) {
                existingBike.setRegistrationNo(updatedBikeInputDto.getRegistrationNo());
            }

            bikeRepository.save(existingBike);

            return DtoMapper.mapBikeToDto(existingBike);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }

}
