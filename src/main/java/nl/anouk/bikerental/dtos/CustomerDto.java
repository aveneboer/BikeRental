package nl.anouk.bikerental.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private Long customerId;

    private String firstName;

    private String lastName;

    private String phoneNo;

    private String email;

    private String address;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ReservationDto> reservations;

}
