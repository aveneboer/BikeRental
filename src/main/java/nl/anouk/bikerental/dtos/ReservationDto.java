package nl.anouk.bikerental.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore
    private ReservationLineDto reservationLine;
    private Long bikeId;
    private int bikeQuantity;



}