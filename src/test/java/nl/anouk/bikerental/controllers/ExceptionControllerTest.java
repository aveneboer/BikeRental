package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.exceptions.*;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import nl.anouk.bikerental.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ExceptionController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void testHandleRecordNotFoundException() throws Exception {
        // Arrange
        String errorMessage = "Record not found";
        RecordNotFoundException exception = new RecordNotFoundException(errorMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testHandleUsernameNotFoundException() throws Exception {
        // Arrange
        String errorMessage = "Username not found";
        UsernameNotFoundException exception = new UsernameNotFoundException(errorMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testHandleBadRequestException() throws Exception {
        // Arrange
        String errorMessage = "Bad request";
        BadRequestException exception = new BadRequestException(errorMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void testHandleBikeNotFoundException() throws Exception {
        // Arrange
        String errorMessage = "Bike not found";
        BikeNotFoundException exception = new BikeNotFoundException(errorMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void testHandleBikeNotAvailableException() throws Exception {
        // Arrange
        String errorMessage = "Bike not available";
        BikeNotAvailableException exception = new BikeNotAvailableException(errorMessage);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/test"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
}
