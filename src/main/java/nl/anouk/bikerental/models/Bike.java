package nl.anouk.bikerental.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "bikes")
@Inheritance
public class Bike extends Vehicle {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "brands")
    private String brand;
    @Column(name = "sizes")
    private String size;

    @Column(name = "registration_numbers")
    private Long registrationNo;

    public Bike(Long vehicleId, String type, boolean availability, double hourlyPrice, Long id, String brand, String size, Long registrationNo) {
        super(vehicleId, type, availability, hourlyPrice);
        this.id = id;
        this.brand = brand;
        this.size = size;
        this.registrationNo = registrationNo;
    }
}
