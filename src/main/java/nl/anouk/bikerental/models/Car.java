package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "cars")
public class Car {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "model")
    private String model;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "capacity")
    private int passenger;
    @Column(name = "is_Available")
    private Boolean isAvailable;
    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations;


}
