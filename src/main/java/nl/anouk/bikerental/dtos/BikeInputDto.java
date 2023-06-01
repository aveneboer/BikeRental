package nl.anouk.bikerental.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class BikeInputDto {
    private String brand;
    private String size;
    private Long registrationNo;

}
