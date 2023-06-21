package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.exceptions.BikeNotAvailableException;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BikeService bikeService;
    private final DtoMapper dtoMapper;

    public ReservationService(ReservationRepository reservationRepository, BikeService bikeService, DtoMapper dtoMapper) {
        this.reservationRepository = reservationRepository;
        this.bikeService = bikeService;
        this.dtoMapper = dtoMapper;
    }


    public void createReservation(Long bikeId, LocalDate startDate, LocalDate endDate) {
        boolean isAvailable = bikeService.checkBikeAvailability(bikeId, startDate, endDate);

        if (!isAvailable) {
            throw new BikeNotAvailableException("De fiets is niet beschikbaar voor de opgegeven datums.");
        }
        //Aanpassen!!!!!! map  maken!
        Reservation reservation = new Reservation();
        Reservation reservation = DtoMapper.mapReservationInputDtoToEntity(reservationInputDto);
        // Bewerk de beschikbaarheid van de fiets
        bikeService.updateBikeAvailability(bikeId, false);
        // Sla de reservering op
        Reservation savedReservation = reservationRepository.save(reservation);
        return;DtoMapper.mapReservationInputDtoToEntity((savedReservation));
    }



    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return DtoMapper.mapReservationListToDtoList(reservations);
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = findReservationById(id);
        return DtoMapper.mapReservationToDto(reservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        reservationRepository.delete(reservation);
    }

    public ReservationDto updateReservation(Long id, ReservationInputDto newReservation) {
        Reservation reservation = findReservationById(id);

        if (newReservation.getStartDate() != null) {
            reservation.setStartDate(newReservation.getStartDate());
        }

        if (newReservation.getEndDate() != null) {
            reservation.setEndDate(newReservation.getEndDate());
        }

        if (newReservation.getType() != null) {
            reservation.setType(newReservation.getType());
        }

        Reservation updatedReservation = reservationRepository.save(reservation);
        return DtoMapper.mapReservationToDto(updatedReservation);
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Reservation not found"));
    }
}

