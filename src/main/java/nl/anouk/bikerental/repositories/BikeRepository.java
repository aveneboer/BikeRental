package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.Bike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    List<Bike> findAllBikesByBrandEqualsIgnoreCase(String Brand);

    List<Bike> findAll();

    Optional<Bike> findAllById(Long id);
}
