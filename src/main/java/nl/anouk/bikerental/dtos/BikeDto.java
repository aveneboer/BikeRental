
package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeDto {

    private Long id;
    private String brand;
    private Long registrationNo;
    private BigDecimal hourlyPrice;
    private Boolean isAvailable;

}


