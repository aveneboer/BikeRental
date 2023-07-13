package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r WHERE r.endDate >= CURRENT_DATE")
    List<Reservation> findActiveReservationsByBike(Bike bike);
    Optional<Reservation> findById(Long id);
}
