
package nl.anouk.bikerental.services;


import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.repositories.ReservationLineRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationLineService {
    private final ReservationLineRepository reservationLineRepository;
    private final ReservationRepository reservationRepository;

    private final BikeService bikeService;
    private final CarService carService;
    private final ReservationService reservationService;

    public ReservationLineService(ReservationLineRepository reservationLineRepository, BikeService bikeService, CarService carService, ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationLineRepository = reservationLineRepository;
        this.bikeService = bikeService;
        this.carService = carService;
        this.reservationService = reservationService;
        this. reservationRepository = reservationRepository;

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


    public ReservationLine createReservationLine(Long reservationId) {
        // Haal de Reservation op aan de hand van het reservationId
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setDateOrdered(LocalDateTime.now());
        reservationLine.setConfirmation("confirmed");


        // Bereken de duur op basis van de startdatum en einddatum
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        Duration duration = Duration.between(startDate, endDate);
        int durationInDays = (int) duration.toDays();
        reservationLine.setDuration(durationInDays);

        // Bereken de totale prijs op basis van de duur, uurprijs en aantal fietsen
        double hourlyPrice = 15; // Vervang dit met de werkelijke uurprijs
        int bikeQuantity = reservation.getBikeQuantity();
        double totalPrice = durationInDays * 6 * hourlyPrice * bikeQuantity; // Aangenomen dat een dag gelijk is aan 6 uur
        reservationLine.setTotalPrice(totalPrice);

        reservationLineRepository.save(reservationLine);

        return reservationLine;
    }}


        /*
    public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {
        ReservationLine reservationLine = DtoMapper.mapReservationLineInputDtoToEntity(reservationLineInputDto);

        // Bereken de duration op basis van de start- en einddatum van de Reservation
        LocalDate startDate = reservationDto.getStartDate();
        LocalDate endDate = reservationDto.getEndDate();
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
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(startDate)) {
            return "Pending";
        } else {
            return "Confirmed";
        }
}

/*
 public ReservationLineDto createReservationLine(ReservationDto reservationDto, ReservationLineInputDto reservationLineInputDto) {
        LocalDate startDate = reservationDto.getStartDate();
        LocalDate endDate = reservationDto.getEndDate();

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
