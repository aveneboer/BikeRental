package nl.anouk.bikerental.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    private Long customerId;
    @NotBlank
    private String type;

    private CustomerDto customer;

    private Long bikeId;
    @NotNull
    private int bikeQuantity;

    private List<Long> bikeIds;
    private ReservationLineDto reservationLine;

}
