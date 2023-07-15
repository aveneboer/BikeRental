package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverLicenseRepository extends JpaRepository<DriverLicense,Long> {

}
