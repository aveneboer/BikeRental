package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.exceptions.BikeNotAvailableException;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.models.Bike;
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
    private final CustomerService customerService;

    public ReservationService(ReservationRepository reservationRepository, BikeService bikeService, CustomerService customerService, DtoMapper dtoMapper) {
        this.reservationRepository = reservationRepository;
        this.bikeService = bikeService;
        this.customerService = customerService;
        this.dtoMapper = dtoMapper;
    }



    public ReservationDto createReservation(ReservationInputDto reservationInputDto) {
        LocalDate startDate = reservationInputDto.getStartDate();
        LocalDate endDate = reservationInputDto.getEndDate();
        int bikeQuantity = reservationInputDto.getBikeQuantity();
        CustomerDto customerDto = reservationInputDto.getCustomer();

        List<Long> bikeIds = reservationInputDto.getBikeIds();
        // Controleer beschikbaarheid van alle fietsen
        for (Long bikeId : bikeIds) {
            boolean isAvailable = bikeService.isBikeAvailable(startDate, endDate, bikeQuantity);
            if (!isAvailable) {
                throw new BikeNotAvailableException("Een van de fietsen is niet beschikbaar voor de opgegeven datums.");
            }
        }

        // Maak nieuwe reservering
        Reservation reservation = new Reservation();
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setBikeQuantity(bikeQuantity);

        // Maak nieuwe klant
        Customer customer = DtoMapper.mapCustomerDtoToEntity(customerDto);
        reservation.setCustomer(customer);


        // Wijs de fietsen toe aan de reservering op basis van de bikeIds
        for (Long bikeId : bikeIds) {
            Bike bike = bikeService.getBikeById(bikeId);
            reservation.setBike(bike);
        }

        // Pas de beschikbaarheid van de fietsen aan
        for (Long bikeId : bikeIds) {
            bikeService.updateBikeAvailability(bikeId, false);
        }
            Reservation savedReservation = reservationRepository.save(reservation);
            return DtoMapper.mapReservationToDto(savedReservation);

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

