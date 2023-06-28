package nl.anouk.bikerental.repositories;

import nl.anouk.bikerental.models.DriverLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverLicenseRepository extends JpaRepository<DriverLicense,Long> {
    DriverLicense findByFilename(String fileName);
}
