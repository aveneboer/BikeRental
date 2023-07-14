package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.ReservationLineDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.ReservationLine;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import nl.anouk.bikerental.services.ReservationLineService;
import nl.anouk.bikerental.services.ReservationService;
import nl.anouk.bikerental.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ReservationLineController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ReservationLineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationLineService reservationLineService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ReservationService reservationService;

    @Test
    public void testGetReservationLine_WithValidId_ReturnsOk() throws Exception {
        // Arrange
        Long reservationLineId = 1L;
        ReservationLineDto reservationLineDto = new ReservationLineDto();
        reservationLineDto.setReservationLineId(reservationLineId);
        Mockito.when(reservationLineService.getReservationLineById(reservationLineId)).thenReturn(reservationLineDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/reservationlines/{id}", reservationLineId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationLineId").value(reservationLineId));
    }

    @Test
    public void testGetReservationLine_WithInvalidId_ReturnsNotFound() throws Exception {
        // Arrange
        Long reservationLineId = 1L;
        Mockito.when(reservationLineService.getReservationLineById(reservationLineId)).thenThrow(RecordNotFoundException.class);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/reservationlines/{id}", reservationLineId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCreateReservationLine_WithValidReservationId_ReturnsOk() throws Exception {
        // Arrange
        Long reservationId = 1L;
        ReservationLine reservationLine = new ReservationLine();
        reservationLine.setReservationLineId(1L);
        Mockito.when(reservationLineService.createReservationLine(reservationId)).thenReturn(reservationLine);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/reservation-line")
                        .param("reservationId", String.valueOf(reservationId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.reservationLineId").exists());
    }


    @Test
    public void testCreateReservationLine_WithInvalidReservationId_ReturnsBadRequest() throws Exception {
        // Arrange
        Long reservationId = 1L;
        Mockito.when(reservationLineService.createReservationLine(reservationId)).thenAnswer(invocation -> {
            throw new Exception("Invalid reservation ID");
        });


        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/reservation-line")
                        .param("reservationId", String.valueOf(reservationId)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetAllReservationLines_ReturnsOk() throws Exception {
        // Arrange
        List<ReservationLineDto> reservationLineDtos = new ArrayList<>();
        Mockito.when(reservationLineService.getAllReservationLines()).thenReturn(reservationLineDtos);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/reservationlines"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }
}