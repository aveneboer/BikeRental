
package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.dtos.BikeInputDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.repositories.BikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BikeService {
    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
    }

    public void deleteBike(@RequestBody Long bikeId) {
        if (bikeRepository.existsById(bikeId)) {
            bikeRepository.deleteById(bikeId);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }

    public List<BikeDto> getAllBikes() {
        List<Bike> bikeList = bikeRepository.findAll();
        return transferBikeListToDtoList(bikeList);
    }

    public List<BikeDto> getAllBikesByBrand(String brand) {
        List<Bike> bikeList = bikeRepository.findAllBikesByBrandEqualsIgnoreCase(brand);
        return transferBikeListToDtoList(bikeList);
    }

    public List<BikeDto> transferBikeListToDtoList(List<Bike> bikes) {
        List<BikeDto> bikeDtoList = new ArrayList<>();

        for(Bike bike : bikes) {
            BikeDto dto = transferToDto(bike);
            bikeDtoList.add(dto);
        }

        return bikeDtoList;
    }

    public BikeDto addBike(BikeInputDto dto) {
        Bike bike = transferToBike(dto);
        bikeRepository.save(bike);

        return transferToDto(bike);
    }
    public BikeDto getBikeById(Long id) {

        if (bikeRepository.findAllById(id).isPresent()) {
            Bike bike = bikeRepository.findAllById(id).get();
            BikeDto dto = transferToDto(bike);

            return transferToDto(bike);
        } else {
            throw new RecordNotFoundException("No bike was found");
        }
    }

    public BikeDto updateBike(Long id, BikeInputDto inputDto) {
        Optional<Bike> optionalBike = bikeRepository.findById(id);

        if (optionalBike.isPresent()) {
            Bike existingBike = optionalBike.get();

            // Update de velden alleen als ze zijn opgegeven in de inputDto
            if (inputDto.getBrand() != null) {
                existingBike.setBrand(inputDto.getBrand());
            }
            if (inputDto.getSize() != null) {
                existingBike.setSize(inputDto.getSize());
            }
            if (inputDto.getRegistrationNo() != null) {
                existingBike.setRegistrationNo(inputDto.getRegistrationNo());
            }
            if (inputDto.getHourlyPrice() != null) {
                existingBike.setHourlyPrice(inputDto.getHourlyPrice());
            }

            bikeRepository.save(existingBike);

            return transferToDto(existingBike);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }




    public Bike transferToBike(BikeInputDto dto) {
        var bike = new Bike();

        bike.setSize(dto.getSize());
        bike.setBrand(dto.getBrand());
        bike.setRegistrationNo(dto.getRegistrationNo());
        bike.setHourlyPrice(dto.getHourlyPrice());

        return bike;
    }


    public BikeDto transferToDto(Bike bike) {
        BikeDto dto = new BikeDto();
        dto.setId(bike.getId());
        dto.setBrand(bike.getBrand());
        dto.setSize(bike.getSize());
        dto.setRegistrationNo(bike.getRegistrationNo());
        dto.setHourlyPrice(bike.getHourlyPrice());

        return dto;
    }

}
