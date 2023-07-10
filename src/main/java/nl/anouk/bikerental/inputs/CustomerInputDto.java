package nl.anouk.bikerental.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.dtos.DriverLicenseDto;
import nl.anouk.bikerental.dtos.ReservationDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInputDto {

    private String firstName;

    private String lastName;

    private String phoneNo;

    private String email;

    private String address;

    private List<ReservationDto> reservations;
    private DriverLicenseDto driverLicense;

}
