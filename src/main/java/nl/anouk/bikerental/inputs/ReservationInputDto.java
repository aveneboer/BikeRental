package nl.anouk.bikerental.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.dtos.ReservationLineDto;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInputDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long customerId;
    private String type;
    private CustomerDto customer;
    private Long bikeId;
    private int bikeQuantity;
    private List<Long> bikeIds;
    private ReservationLineDto reservationLine;

}
