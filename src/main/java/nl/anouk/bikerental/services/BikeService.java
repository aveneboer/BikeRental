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

@Service
public class BikeService {
    private final BikeRepository bikeRepository;

    public BikeService(BikeRepository bikeRepository) {
        this.bikeRepository = bikeRepository;
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
        if (bikeRepository.findAllById(id).isPresent()) {

            Bike bike = bikeRepository.findAllById(id).get();

            Bike bike1 = transferToBike(inputDto);
            bike1.setVehicleId(bike.getVehicleId());

            bikeRepository.save(bike1);

            return transferToDto(bike1);

        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }

    public void deleteBike(@RequestBody Long bikeId) {
        if (bikeRepository.existsById(bikeId)) {
            bikeRepository.deleteById(bikeId);
        } else {
            throw new NoSuchElementException("Bike not found");
        }
    }

    public Bike transferToBike(BikeInputDto dto) {
        var bike = new Bike();

        bike.setSize(dto.getSize());
        bike.setBrand(dto.getBrand());
        bike.setRegistrationNo(dto.getRegistrationNo());

        return bike;
    }

    public BikeDto transferToDto(Bike bike) {
        BikeDto dto = new BikeDto();

        dto.setBrand(bike.getBrand());
        dto.setSize(bike.getSize());
        dto.setRegistrationNo(bike.getRegistrationNo());

        return dto;
    }

}