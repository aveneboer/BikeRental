
package nl.anouk.bikerental.services;


import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.repositories.ReservationLineRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Transactional
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
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setDateOrdered(LocalDateTime.now());
        reservationLine.setConfirmation("confirmed");

        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();
        long durationInDays = ChronoUnit.DAYS.between(startDate, endDate);
        int durationInDaysInt = Math.toIntExact(durationInDays);
        reservationLine.setDuration(durationInDaysInt);

        double totalPrice;
        if (reservation.getType().equalsIgnoreCase("bike")) {
            double hourlyPrice = 20000;
            int bikeQuantity = reservation.getBikeQuantity();
            totalPrice = durationInDaysInt * 8 * hourlyPrice * bikeQuantity;
        } else if (reservation.getType().equalsIgnoreCase("car")) {
            double dayPrice = 800000;
            totalPrice = durationInDaysInt * dayPrice;
        } else {
            throw new RuntimeException("Invalid reservation type");
        }
        reservationLine.setTotalPrice(totalPrice);

        reservationLineRepository.save(reservationLine);

        return reservationLine;
    }
}
