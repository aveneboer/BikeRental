package nl.anouk.bikerental.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInputDto {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long vehicleId;
    private Long customerId;
/*    private List<ReservationLineInputDto> reservationLines;*/


}