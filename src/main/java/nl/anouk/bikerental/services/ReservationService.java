package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.ReservationDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.inputs.ReservationInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.models.Reservation;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.repositories.CustomerRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BikeRepository bikeRepository;
    private final CustomerRepository customerRepository;
    private final BikeService bikeService;
    private final DtoMapper dtoMapper;
    private final CustomerService customerService;

    public ReservationService(ReservationRepository reservationRepository, BikeService bikeService, CustomerService customerService, CustomerRepository customerRepository, DtoMapper dtoMapper, BikeRepository bikeRepository) {
        this.reservationRepository = reservationRepository;
        this.bikeService = bikeService;
        this.customerService = customerService;
        this.dtoMapper = dtoMapper;
        this.customerRepository = customerRepository;
        this.bikeRepository = bikeRepository;
    }

    public Reservation createReservation(ReservationInputDto inputDto) {
        Reservation reservation = DtoMapper.mapReservationInputDtoToEntity(inputDto);
        LocalDate startDate = inputDto.getStartDate();
        LocalDate endDate = inputDto.getEndDate();
        int bikeQuantity = inputDto.getBikeQuantity();

        boolean areBikesAvailable = bikeService.areBikesAvailable(startDate, endDate, bikeQuantity);

        if (!areBikesAvailable) {

            throw new IllegalArgumentException("Requested bikes are not available during this period.");
        }

        List<Long> availableBikeIds = bikeService.getAvailableBikeIds(startDate, endDate, bikeQuantity);

        if (availableBikeIds.size() < bikeQuantity) {

            throw new IllegalArgumentException("There are not enough bikes available during this period.");
        }

        List<Bike> bikes = new ArrayList<>();
        for (Long bikeId : availableBikeIds) {
            Bike bike = bikeService.getBikeById(bikeId);
            bikes.add(bike);
        }

        reservation.setBikes(bikes);

        Customer customer = reservation.getCustomer();
        customerRepository.save(customer);

        Reservation savedReservation = reservationRepository.save(reservation);


        return reservation;
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

