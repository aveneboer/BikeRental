
package nl.anouk.bikerental.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.dtos.ReservationDto;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLineInputDto {
    private LocalDateTime dateOrdered;
    private String confirmation;

    private int duration;
    private double totalPrice;
    private ReservationDto reservation;
}

