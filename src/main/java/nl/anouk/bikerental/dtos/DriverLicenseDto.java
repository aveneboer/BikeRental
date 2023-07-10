package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DriverLicenseDto {
    private Long id;
    private String filename;
    private byte[] driverLicense;


}
