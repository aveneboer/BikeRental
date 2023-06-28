package nl.anouk.bikerental.models;

import nl.anouk.bikerental.dtos.*;
import nl.anouk.bikerental.inputs.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DtoMapper {
    public static BikeDto mapBikeToDto(Bike bike) {
        BikeDto dto = new BikeDto();
        dto.setId(bike.getId());
        dto.setBrand(bike.getBrand());
        dto.setRegistrationNo(bike.getRegistrationNo());
        dto.setHourlyPrice(bike.getHourlyPrice());
        dto.setIsAvailable(bike.getIsAvailable());
        return dto;
    }

    public static Bike mapBikeInputDtoToEntity(BikeInputDto inputDto) {
        Bike bike = new Bike();
        bike.setBrand(inputDto.getBrand());
        bike.setRegistrationNo(inputDto.getRegistrationNo());
        bike.setHourlyPrice(inputDto.getHourlyPrice());
        bike.setIsAvailable(inputDto.getIsAvailable());
        return bike;
    }

    public static List<BikeDto> mapBikeListToDtoList(List<Bike> bikeList) {
        List<BikeDto> bikeDtoList = new ArrayList<>();

        for (Bike bike : bikeList) {
            BikeDto dto = mapBikeToDto(bike);
            bikeDtoList.add(dto);
        }

        return bikeDtoList;
    }

    public static CarDto mapCarToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setModel(car.getModel());
        dto.setPassenger(car.getPassenger());
        dto.setDayPrice(car.getDayPrice());
        dto.setQuantity(car.getQuantity());
        return dto;
    }

    public static Car mapCarInputDtoToEntity(CarInputDto inputDto) {
        Car car = new Car();
        car.setModel(inputDto.getModel());
        car.setQuantity(inputDto.getQuantity());
        car.setDayPrice(inputDto.getDayPrice());
        car.setPassenger(inputDto.getPassenger());
        return car;
    }


    public static List<CarDto> mapCarListToDtoList(List<Car> cars) {
        List<CarDto> carDtoList = new ArrayList<>();

        for (Car car : cars) {
            CarDto dto = mapCarToDto(car);
            carDtoList.add(dto);
        }

        return carDtoList;
    }
    public static Customer mapCustomerDtoToEntity (CustomerDto Dto) {
        Customer customer = new Customer();
        customer.setFirstName(Dto.getFirstName());
        customer.setLastName(Dto.getLastName());
        customer.setPhoneNo(Dto.getPhoneNo());
        customer.setEmail(Dto.getEmail());
        customer.setAddress(Dto.getAddress());
        return customer;
    }


    public static List<CustomerDto> mapCustomerListToDtoList(List<Customer> customers) {
        return customers.stream()
                .map(customer -> mapCustomerToDto(customer, true))  // Gebruik includeReservations=true
                .collect(Collectors.toList());
    }


    public static CustomerDto mapCustomerToDto(Customer customer, boolean includeReservations) {
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customer.getCustomerId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhoneNo(customer.getPhoneNo());
        dto.setEmail(customer.getEmail());
        dto.setAddress(customer.getAddress());


        if (includeReservations && customer.getReservations() != null) {
            List<ReservationDto> reservationDtos = new ArrayList<>();

            for (Reservation reservation : customer.getReservations()) {
                ReservationDto reservationDto = new ReservationDto();
                reservationDto.setReservationId(reservation.getReservationId());
                reservationDto.setStartDate(reservation.getStartDate());
                reservationDto.setEndDate(reservation.getEndDate());
                reservationDto.setType(reservation.getType());

                reservationDtos.add(reservationDto);
            }

            dto.setReservations(reservationDtos);
        }
        return dto;
    }



    public static Customer mapDtoToCustomer(CustomerInputDto dto) {
        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhoneNo(dto.getPhoneNo());
        customer.setEmail(dto.getEmail());
        customer.setAddress(dto.getAddress());
        return customer;
    }

    public static Customer mapCustomerInputDtoToEntity(CustomerInputDto inputDto) {
        Customer customer = new Customer();
        customer.setFirstName(inputDto.getFirstName());
        customer.setLastName(inputDto.getLastName());
        customer.setPhoneNo(inputDto.getPhoneNo());
        customer.setEmail(inputDto.getEmail());
        customer.setAddress(inputDto.getAddress());

        return customer;
    }



   public static Reservation mapReservationInputDtoToEntity(ReservationInputDto inputDto) {
        Reservation reservation = new Reservation();
        reservation.setStartDate(inputDto.getStartDate());
        reservation.setEndDate(inputDto.getEndDate());
        reservation.setType(inputDto.getType());
        reservation.setBikeQuantity(inputDto.getBikeQuantity());

        Customer customer = new Customer();
        customer.setFirstName(inputDto.getCustomer().getFirstName());
        customer.setLastName(inputDto.getCustomer().getLastName());
        customer.setPhoneNo(inputDto.getCustomer().getPhoneNo());
        customer.setEmail(inputDto.getCustomer().getEmail());
        customer.setAddress(inputDto.getCustomer().getAddress());

        reservation.setCustomer(customer);

        return reservation;

    }

    public static ReservationDto mapReservationToDto(Reservation reservation) {
        ReservationDto dto = new ReservationDto();
        dto.setReservationId(reservation.getReservationId());
        dto.setStartDate(reservation.getStartDate());
        dto.setEndDate(reservation.getEndDate());
        dto.setType(reservation.getType());
        dto.setBikeQuantity(reservation.getBikeQuantity());

        CustomerDto customerDto = mapCustomerToDto(reservation.getCustomer(), false);
        dto.setCustomer(customerDto);

        return dto;
    }

    public static List<ReservationDto> mapReservationListToDtoList(List<Reservation> reservations) {
        return reservations.stream()
                .map(nl.anouk.bikerental.models.DtoMapper::mapReservationToDto)
                .collect(Collectors.toList());
    }

    public static ReservationLine mapReservationLineInputDtoToEntity(ReservationLineInputDto inputDto) {
        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setConfirmation(inputDto.getConfirmation());


        return reservationLine;
    }


    public static ReservationLineDto mapReservationLineToDto(ReservationLine reservationLine) {
        ReservationLineDto dto = new ReservationLineDto();

        dto.setReservationLineId(reservationLine.getReservationLineId());
        dto.setDateOrdered(reservationLine.getDateOrdered());
        dto.setConfirmation(reservationLine.getConfirmation());

        dto.setDuration(reservationLine.getDuration());
        dto.setTotalPrice(reservationLine.getTotalPrice());

        if (reservationLine.getReservation() != null) {
            ReservationDto reservationDto = mapReservationToDto(reservationLine.getReservation());
            dto.setReservation(reservationDto);
        }

        return dto;
    }


    public static List<ReservationLineDto> mapReservationLineListToDtoList(List<ReservationLine> reservationLines) {
        return reservationLines.stream()
                .map(nl.anouk.bikerental.models.DtoMapper::mapReservationLineToDto)
                .collect(Collectors.toList());
    }
}
