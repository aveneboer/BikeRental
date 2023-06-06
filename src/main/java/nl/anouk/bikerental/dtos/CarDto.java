
package nl.anouk.bikerental.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Long id;

    private String model;

    private int passenger;

    private BigDecimal dayPrice;
}

