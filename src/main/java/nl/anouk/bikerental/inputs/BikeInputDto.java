
package nl.anouk.bikerental.inputs;

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
    private int quantity;
    private Long registrationNo;
    private BigDecimal hourlyPrice;

}

