package nl.anouk.bikerental.controllers;


import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DriverLicense;
import nl.anouk.bikerental.services.DriverLicenseService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/driverLicense")
public class DriverLicenseController {
    private final DriverLicenseService driverLicenseService;

    public DriverLicenseController(DriverLicenseService driverLicenseService) {
        this.driverLicenseService = driverLicenseService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadDriverLicense(@RequestParam("file") MultipartFile file) {
        try {
            DriverLicense driverLicense = driverLicenseService.saveDriverLicense(file);
            return ResponseEntity.ok("DriverLicense uploaded successfully. ID: " + driverLicense.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading DriverLicense");
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadDriverLicense(@PathVariable Long id) {
        try {
            DriverLicense driverLicense = driverLicenseService.getDriverLicense(id);
            byte[] driverLicenseContent = driverLicense.getDriverLicense();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "driverLicense_" + id + ".pdf");
            headers.setContentLength(driverLicenseContent.length);

            return new ResponseEntity<>(driverLicenseContent, headers, HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "DriverLicense not found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}


