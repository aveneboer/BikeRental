
package nl.anouk.bikerental.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "reservation_lines")
public class ReservationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationLineId;

    @Column(name = "date_ordered")
    private LocalDateTime dateOrdered;

    @Column(name = "confirmation")
    private String confirmation;

    @Column(name = "status")
    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;


    @Column(name = "duration")
    private int duration;

    @Column(name = "total_price")
    private double totalPrice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", insertable = false, updatable = false)
    private Reservation reservation;

}
