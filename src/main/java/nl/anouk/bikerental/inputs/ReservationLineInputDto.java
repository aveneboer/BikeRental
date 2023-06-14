package nl.anouk.bikerental.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLineInputDto {
    private LocalDateTime dateOrdered;
    private String confirmation;
    private String status;
    private String paymentMethod;
    private int duration;
    private double totalPrice;
}
