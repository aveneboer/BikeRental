package nl.anouk.bikerental.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    @Column(name = "hourly_price")
    private BigDecimal hourlyPrice;

}
