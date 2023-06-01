package nl.anouk.bikerental.repositories;

import org.springframework.stereotype.Repository;


@Repository
public interface BikeRepository extends JpaRepository<Bike, Long> {
    List<Bike> findAllBikesByBrandEqualsIgnoreCase(String Brand);

    List<Bike> findAll();

    Optional<Bike> findAllById(Long id);
}
