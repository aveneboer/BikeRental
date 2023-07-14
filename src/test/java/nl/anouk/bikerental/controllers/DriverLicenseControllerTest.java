package nl.anouk.bikerental.controllers;


import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.DriverLicense;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import nl.anouk.bikerental.services.DriverLicenseService;
import nl.anouk.bikerental.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@WebMvcTest(DriverLicenseController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class DriverLicenseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriverLicenseService driverLicenseService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testUploadDriverLicense_Success() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "driverLicense.pdf", MediaType.APPLICATION_PDF_VALUE, "Some content".getBytes());
        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setId(1L);
        Mockito.when(driverLicenseService.saveDriverLicense(Mockito.any(MultipartFile.class))).thenReturn(driverLicense);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/driverLicense/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("DriverLicense uploaded successfully. ID: 1"));
        Mockito.verify(driverLicenseService, Mockito.times(1)).saveDriverLicense(Mockito.any(MultipartFile.class));
    }

    @Test
    public void testUploadDriverLicense_Error() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "driverLicense.pdf", MediaType.APPLICATION_PDF_VALUE, "Some content".getBytes());
        Mockito.when(driverLicenseService.saveDriverLicense(Mockito.any(MultipartFile.class))).thenThrow(IOException.class);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/driverLicense/upload")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Error uploading DriverLicense"));
        Mockito.verify(driverLicenseService, Mockito.times(1)).saveDriverLicense(Mockito.any(MultipartFile.class));
    }

    @Test
    public void testDownloadDriverLicense_Success() throws Exception {
        // Arrange
        Long driverLicenseId = 1L;
        DriverLicense driverLicense = new DriverLicense();
        driverLicense.setId(driverLicenseId);
        driverLicense.setDriverLicense("Some content".getBytes());
        Mockito.when(driverLicenseService.getDriverLicense(driverLicenseId)).thenReturn(driverLicense);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/driverLicense/download/{id}", driverLicenseId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"driverLicense_1.pdf\""))
                .andExpect(MockMvcResultMatchers.content().bytes("Some content".getBytes()));
        Mockito.verify(driverLicenseService, Mockito.times(1)).getDriverLicense(driverLicenseId);
    }

    @Test
    public void testDownloadDriverLicense_NotFound() throws Exception {
        // Arrange
        Long driverLicenseId = 1L;
        Mockito.when(driverLicenseService.getDriverLicense(driverLicenseId)).thenThrow(RecordNotFoundException.class);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/driverLicense/download/{id}", driverLicenseId))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("DriverLicense not found"));
        Mockito.verify(driverLicenseService, Mockito.times(1)).getDriverLicense(driverLicenseId);
    }
}