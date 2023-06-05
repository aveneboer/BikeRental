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
@Entity
@Table(name = "bikes")
public class Bike {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "brands")
    private String brand;
    @Column(name = "sizes")
    private String size;

    @Column(name = "registration_numbers")
    private Long registrationNo;
    @Column(name = "availability")
    private boolean availability;
    @Column(name = "hourly_price")
    private double hourlyPrice;

}
