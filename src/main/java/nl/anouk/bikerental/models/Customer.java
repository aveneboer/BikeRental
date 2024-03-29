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
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToOne(mappedBy = "customer")
    private DriverLicense driverLicense;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public void setUser(String username) {
        User user = new User();
        user.setUsername(username);
        this.user = user;
        user.setCustomer(this);
}
}
