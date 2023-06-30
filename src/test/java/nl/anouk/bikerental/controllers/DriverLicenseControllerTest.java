package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DriverLicense;
import nl.anouk.bikerental.services.DriverLicenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DriverLicenseControllerTest {
        @Mock
        private DriverLicenseService driverLicenseService;

        @InjectMocks
        private DriverLicenseController driverLicenseController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testUploadDriverLicense_ValidFile_ReturnsSuccessMessage() throws java.io.IOException {
            // Arrange
            MultipartFile file = mock(MultipartFile.class);
            DriverLicense driverLicense = new DriverLicense();
            driverLicense.setId(1L);
            given(driverLicenseService.saveDriverLicense(file)).willReturn(driverLicense);

            // Act
            ResponseEntity<String> response = driverLicenseController.uploadDriverLicense(file);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("DriverLicense uploaded successfully. ID: 1", response.getBody());
            verify(driverLicenseService).saveDriverLicense(file);
        }

        @Test
        void testUploadDriverLicense_IOException_ReturnsInternalServerError() throws java.io.IOException {
            // Arrange
            MultipartFile file = mock(MultipartFile.class);
            given(driverLicenseService.saveDriverLicense(file)).willThrow(IOException.class);

            // Act
            ResponseEntity<String> response = driverLicenseController.uploadDriverLicense(file);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals("Error uploading DriverLicense", response.getBody());
            verify(driverLicenseService).saveDriverLicense(file);
        }

    @Test
    void testDownloadDriverLicense_ValidId_ReturnsDriverLicenseContent() throws RecordNotFoundException {
        // Arrange
        Long id = 1L;
        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setDriverLicense(new byte[10]);
        given(driverLicenseService.getDriverLicense(id)).willReturn(driverLicense);

        // Act
        ResponseEntity<?> response = driverLicenseController.downloadDriverLicense(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentDisposition().toString().contains("attachment"));
        assertEquals(driverLicense.getDriverLicense().length, response.getHeaders().getContentLength());
        assertEquals(driverLicense.getDriverLicense(), response.getBody());
        verify(driverLicenseService).getDriverLicense(id);
    }

        @Test
        void testDownloadDriverLicense_RecordNotFoundException_ReturnsNotFound() throws RecordNotFoundException {
            // Arrange
            Long id = 1L;
            given(driverLicenseService.getDriverLicense(id)).willThrow(RecordNotFoundException.class);

            // Act
            ResponseEntity<?> response = driverLicenseController.downloadDriverLicense(id);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "DriverLicense not found");
            assertEquals(errorResponse, response.getBody());
            verify(driverLicenseService).getDriverLicense(id);
        }
    }