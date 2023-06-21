
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.exceptions.BikeNotAvailableException;
import nl.anouk.bikerental.exceptions.BikeNotFoundException;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.BikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
// check availability and change availability when a reservation is made

        public boolean checkBikeAvailability(Long bikeId, LocalDate startDate, LocalDate endDate) {
            Bike bike = bikeRepository.findById(bikeId)
                    .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));

            return isBikeAvailable(bike, startDate, endDate);
        }

        private boolean isBikeAvailable(Bike bike, LocalDate startDate, LocalDate endDate) {
            for (Reservation reservation : bike.getReservations()) {
                if (startDate.isBefore(reservation.getEndDate()) && endDate.isAfter(reservation.getStartDate())) {
                    return false;
                }
            }
            return true;
        }

        public void rentBike(Long bikeId) {
            Bike bike = bikeRepository.findById(bikeId)
                    .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));

            if (!bike.isAvailable()) {
                throw new BikeNotAvailableException("De fiets is niet beschikbaar.");
            }

            bike.setIsAvailable(false);
            bikeRepository.save(bike);
        }

    //einde
/*    public BikeDto getBikeById(Long id) {
        Optional<Bike> optionalBike = bikeRepository.findById(id);

        if (optionalBike.isPresent()) {
            Bike bike = optionalBike.get();
            return DtoMapper.mapBikeToDto(bike);
        } else {
            throw new RecordNotFoundException("No bike was found");
        }
    }*/
    public Bike getBikeById(Long bikeId) {
        return bikeRepository.findById(bikeId)
                .orElseThrow(() -> new BikeNotFoundException("Fiets met ID " + bikeId + " niet gevonden."));
    }
    public List<BikeDto> getAvailableBikes() {
        List<Bike> availableBikes = bikeRepository.findAllByIsAvailable(true);
        return DtoMapper.mapBikeListToDtoList(availableBikes);
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

    public BikeDto getBikeDto() {
        Bike bike = bikeRepository.findBike();

        BikeDto bikeDto = new BikeDto();
        bikeDto.setHourlyPrice(bike.getHourlyPrice());
        bikeDto.setQuantity(bike.getQuantity());

        return bikeDto;
    }

    public BikeDto transferToDto(Bike bike) {
        BikeDto dto = new BikeDto();

        dto.setBrand(bike.getBrand());
        dto.setQuantity(bike.getQuantity());
        dto.setRegistrationNo(bike.getRegistrationNo());
        dto.setHourlyPrice(bike.getHourlyPrice());

        return dto;
    }
    public List<BikeDto> getAllBikesByBrand(String brand) {
        List<Bike> bikeList = bikeRepository.findAllBikesByBrandEqualsIgnoreCase(brand);
        return DtoMapper.mapBikeListToDtoList(bikeList);
    }

}
