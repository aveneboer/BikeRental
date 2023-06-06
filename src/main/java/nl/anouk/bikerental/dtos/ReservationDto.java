package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private Long reservationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
 /*   private CustomerDto customer;
    private List<ReservationLineDto> reservationLines;*/

}