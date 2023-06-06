
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

public class BikeInputDto {
    private String brand;
    private String size;
    private Long registrationNo;
    private BigDecimal hourlyPrice;

}


