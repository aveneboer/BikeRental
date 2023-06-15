
package nl.anouk.bikerental.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.models.ReservationLine;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLineDto {
    private Long reservationLineId;
    private LocalDateTime dateOrdered;
    private String confirmation;
    private String status;
    private String paymentMethod;
    private int duration;
    private double totalPrice;
    private ReservationDto reservation;

}

