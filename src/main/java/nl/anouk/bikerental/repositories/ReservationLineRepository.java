package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.ReservationLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationLineRepository extends JpaRepository<ReservationLine, Long> {

}
