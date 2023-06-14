package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.*;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.inputs.ReservationLineInputDto;
import nl.anouk.bikerental.models.*;
import nl.anouk.bikerental.repositories.ReservationLineRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ReservationLineService {
    private final ReservationLineRepository reservationLineRepository;
    private final BikeService bikeService;
    private final CarService carService;

    public ReservationLineService(ReservationLineRepository reservationLineRepository, BikeService bikeService, CarService carService) {
        this.reservationLineRepository = reservationLineRepository;
        this.bikeService = bikeService;
        this.carService = carService;
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
    }


/*    public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {
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
    }*/

}
