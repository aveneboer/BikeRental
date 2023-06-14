package nl.anouk.bikerental.inputs;
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
public class ReservationInputDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Long customerId;
    private String type;
    private ReservationLine reservationLine;



}
