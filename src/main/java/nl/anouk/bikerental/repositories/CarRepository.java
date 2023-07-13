
package nl.anouk.bikerental.repositories;


import nl.anouk.bikerental.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllCarsByModelEqualsIgnoreCase(String model);
    List<Car> findAllByIsAvailable(Boolean isAvailable);

    List<Car> findAll();

    List<Car> findAllByPassengerGreaterThanEqual(int passenger);
    Optional<Car> findById(Long carId);

    @Query("SELECT b FROM Car b")
    Car findCar();
}


