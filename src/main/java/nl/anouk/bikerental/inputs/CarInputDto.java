

package nl.anouk.bikerental.inputs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarInputDto {

    private String model;

    private int passenger;
    private int quantity;
}

