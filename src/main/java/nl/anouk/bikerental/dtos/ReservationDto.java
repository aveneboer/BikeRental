package nl.anouk.bikerental.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.anouk.bikerental.models.Customer;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
/*    private ReservationLineDto reservationLine;*/


}