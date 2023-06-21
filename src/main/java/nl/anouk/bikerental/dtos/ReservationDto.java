package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.models.Bike;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    public Long reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private CustomerDto customer;
    private ReservationLineDto reservationLine;
    private Bike bike;


}