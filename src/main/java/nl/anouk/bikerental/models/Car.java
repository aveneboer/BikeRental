package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "capacity")
    private int passenger;
    @Column(name = "day_price")
    private double DayPrice;

}
