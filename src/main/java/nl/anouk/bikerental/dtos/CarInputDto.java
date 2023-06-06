

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
public class CarInputDto {

    private String model;

    private int passenger;
    private BigDecimal dayPrice;
}

