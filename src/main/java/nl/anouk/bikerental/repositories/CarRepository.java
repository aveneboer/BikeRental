package nl.anouk.bikerental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllCarsByModelEqualsIgnoreCase(String Model);

    List <Car> findAll();

    Optional<Car> findAllById(Long id);

    Optional<Car> findAllByCapacity(int passenger);

}
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findAllCarsByModelEqualsIgnoreCase(String Model);

    List <Car> findAll();

    Optional<Car> findAllById(Long id);

    Optional<Car> findAllByCapacity(int passenger);

}
