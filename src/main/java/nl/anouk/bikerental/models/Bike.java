package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Column(name = "registration_numbers")
    private Long registrationNo;

    @Column(name = "is_Available")
    private Boolean isAvailable;

    @ManyToMany(mappedBy = "bikes", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

}
