
package nl.anouk.bikerental.services;


import nl.anouk.bikerental.dtos.*;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;

import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.inputs.ReservationLineInputDto;
import nl.anouk.bikerental.models.*;
import nl.anouk.bikerental.repositories.ReservationLineRepository;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationLineService {
    private final ReservationLineRepository reservationLineRepository;
    private final BikeService bikeService;
    private final CarService carService;
    private final ReservationService reservationService;

    public ReservationLineService(ReservationLineRepository reservationLineRepository, BikeService bikeService, CarService carService, ReservationService reservationService) {
        this.reservationLineRepository = reservationLineRepository;
        this.bikeService = bikeService;
        this.carService = carService;
        this.reservationService = reservationService;
    }

    public ReservationLineDto getReservationLineById(Long id) {
        ReservationLine reservationLine = reservationLineRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation line not found"));

        return DtoMapper.mapReservationLineToDto(reservationLine);
    }

    public List<ReservationLineDto> getAllReservationLines() {
        List<ReservationLine> reservationLines = reservationLineRepository.findAll();
        return DtoMapper.mapReservationLineListToDtoList(reservationLines);
    }

    public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {

            ReservationLine reservationLine = DtoMapper.mapReservationLineInputDtoToEntity(reservationLineInputDto);
            ReservationLine savedReservationLine = reservationLineRepository.save(reservationLine);

            return DtoMapper.mapReservationLineToDto(savedReservationLine);
        }}

        /*
    public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {
        ReservationLine reservationLine = DtoMapper.mapReservationLineInputDtoToEntity(reservationLineInputDto);

        // Bereken de duration op basis van de start- en einddatum van de Reservation
        LocalDateTime startDate = reservationDto.getStartDate();
        LocalDateTime endDate = reservationDto.getEndDate();
        int duration = calculateDuration(startDate, endDate);
        reservationLine.setDuration(duration);
        reservationLine.setDateOrdered(LocalDateTime.now());
        reservationLine.setConfirmation(generateConfirmationCode());
        reservationLine.setStatus(getInitialStatus(startDate));
        // Andere toewijzingen en opslaglogica...

        ReservationLine savedReservationLine = reservationLineRepository.save(reservationLine);

        return DtoMapper.mapReservationLineToDto(savedReservationLine);
    }

    private int calculateDuration(LocalDateTime startDate, LocalDateTime endDate) {
        // Implementeer de logica om de duur te berekenen op basis van de start- en einddatum
        // Dit kan bijvoorbeeld gedaan worden met behulp van de java.time.Duration klasse

        // Voorbeeld: Bereken het verschil in uren tussen de start- en einddatum
        Duration duration = Duration.between(startDate, endDate);
        long hours = duration.toHours();

        return (int) hours; // Converteer naar een integer (afhankelijk van je vereisten)
    }
    private String generateConfirmationCode() {
        // Implementeer de logica om een bevestigingscode te genereren, bijvoorbeeld met behulp van UUID

        return UUID.randomUUID().toString();
    }
    private String getInitialStatus(LocalDateTime startDate) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(startDate)) {
            return "Pending";
        } else {
            return "Confirmed";
        }
}

/*
 public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {
        LocalDateTime startDate = reservationDto.getStartDate();
        LocalDateTime endDate = reservationDto.getEndDate();

        int durationInHours = calculateDurationInHours(startDate, endDate);

        reservationLineInputDto.setDuration(durationInHours > 0 ? durationInHours : 0);

        String reservationType = reservationDto.getType();
        BigDecimal hourlyPrice;
        BigDecimal dayPrice;
        int quantity;

        double totalPrice;

        if (reservationType.equalsIgnoreCase("bike")) {
            BikeDto bikeDto = bikeService.getBikeDto();
            hourlyPrice = bikeDto.getHourlyPrice();
            quantity = bikeDto.getQuantity();
            totalPrice = hourlyPrice.multiply(BigDecimal.valueOf(durationInHours)).multiply(BigDecimal.valueOf(quantity)).doubleValue();
        } else if (reservationType.equalsIgnoreCase("car")) {
            CarDto carDto = carService.getCarDto();
            dayPrice = carDto.getDayPrice();
            totalPrice = dayPrice.multiply(BigDecimal.valueOf(durationInHours / 6.0)).doubleValue();
        } else {
            throw new IllegalArgumentException("Invalid reservation type");
        }

        reservationLineInputDto.setTotalPrice(totalPrice);

        ReservationLine reservationLine = DtoMapper.mapReservationLineInputDtoToEntity(reservationLineInputDto);
        ReservationLine savedReservationLine = reservationLineRepository.save(reservationLine);

        return DtoMapper.mapReservationLineToDto(savedReservationLine);
    }

    private int calculateDurationInHours(LocalDateTime startDate, LocalDateTime endDate) {
        Duration duration = Duration.between(startDate, endDate);
        long hours = duration.toHours();
        return Math.toIntExact(hours);
    }
*//*

}
*/
