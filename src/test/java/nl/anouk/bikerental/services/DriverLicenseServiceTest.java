package nl.anouk.bikerental.services;

import io.jsonwebtoken.io.IOException;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DriverLicense;
import nl.anouk.bikerental.repositories.DriverLicenseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DriverLicenseServiceTest {
    @InjectMocks
    private DriverLicenseService driverLicenseService;

    @Mock
    private DriverLicenseRepository driverLicenseRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        driverLicenseService = new DriverLicenseService(driverLicenseRepository);
    }

    @Test
    public void testSaveDriverLicense_Success() throws IOException, java.io.IOException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.txt", "Test data".getBytes());
        Mockito.when(driverLicenseRepository.save(Mockito.any(DriverLicense.class))).thenReturn(new DriverLicense());

        // Act
        DriverLicense result = driverLicenseService.saveDriverLicense(file);

        // Assert
        Assertions.assertNotNull(result);
        Mockito.verify(driverLicenseRepository, Mockito.times(1)).save(Mockito.any(DriverLicense.class));
    }

    @Test
    public void testSaveDriverLicense_EmptyFile() {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.txt", "".getBytes());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            driverLicenseService.saveDriverLicense(file);
        });
        Mockito.verify(driverLicenseRepository, Mockito.never()).save(Mockito.any(DriverLicense.class));
    }

    @Test
    public void testGetDriverLicense_Success() {
        // Arrange
        Long driverLicenseId = 1L;
        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setId(driverLicenseId);
        Mockito.when(driverLicenseRepository.findById(driverLicenseId)).thenReturn(Optional.of(driverLicense));

        // Act
        DriverLicense result = driverLicenseService.getDriverLicense(driverLicenseId);

        // Assert
        Assertions.assertEquals(driverLicenseId, result.getId());
        Mockito.verify(driverLicenseRepository, Mockito.times(1)).findById(driverLicenseId);
    }

    @Test
    public void testGetDriverLicense_RecordNotFoundException() {
        // Arrange
        Long driverLicenseId = 1L;
        Mockito.when(driverLicenseRepository.findById(driverLicenseId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RecordNotFoundException.class, () -> {
            driverLicenseService.getDriverLicense(driverLicenseId);
        });
        Mockito.verify(driverLicenseRepository, Mockito.times(1)).findById(driverLicenseId);
    }
}