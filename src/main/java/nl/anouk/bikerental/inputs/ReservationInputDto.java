package nl.anouk.bikerental.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.dtos.ReservationLineDto;

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
    private CustomerDto customer;

    private ReservationLineDto reservationLine;


}
