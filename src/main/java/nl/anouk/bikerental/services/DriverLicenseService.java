package nl.anouk.bikerental.services;

import io.jsonwebtoken.io.IOException;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DriverLicense;
import nl.anouk.bikerental.repositories.DriverLicenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DriverLicenseService {
    private final DriverLicenseRepository fileRepository;

    public DriverLicenseService(DriverLicenseRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public DriverLicense saveDriverLicense(MultipartFile file) throws IOException, java.io.IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }

        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setFilename(file.getOriginalFilename());
        driverLicense.setDriverLicense(file.getBytes());

        return fileRepository.save(driverLicense);
    }

    public DriverLicense getDriverLicense(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No driverLicense found with id: " + id));
    }
}
