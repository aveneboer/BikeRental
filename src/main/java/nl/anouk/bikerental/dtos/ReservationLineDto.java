
package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLineDto {
    private Long reservationLineId;
    private LocalDateTime dateOrdered;
    private String confirmation;

    private int duration;
    private double totalPrice;
    private ReservationDto reservation;

}

