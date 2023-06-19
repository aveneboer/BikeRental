package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    public Long reservationId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String type;
    private CustomerDto customer;
    private ReservationLineDto reservationLine;


}