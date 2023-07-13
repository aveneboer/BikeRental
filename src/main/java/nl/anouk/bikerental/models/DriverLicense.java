package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class DriverLicense {
    @Id
    @GeneratedValue
    private Long id;
    private String filename;
    @Lob
    private byte[] driverLicense;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
