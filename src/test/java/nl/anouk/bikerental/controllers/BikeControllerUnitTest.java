package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.BikeDto;
import nl.anouk.bikerental.inputs.BikeInputDto;
import nl.anouk.bikerental.models.Bike;
import nl.anouk.bikerental.repositories.BikeRepository;
import nl.anouk.bikerental.services.BikeService;
import nl.anouk.bikerental.services.CustomUserDetailsService;
import nl.anouk.bikerental.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
@WebMvcTest(BikeController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class BikeControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BikeService bikeService;
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private BikeRepository bikeRepository;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;



    @Captor
    private ArgumentCaptor<BikeInputDto> bikeInputDtoCaptor;

    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testCheckAvailability_WithValidInput_ReturnsOk() throws Exception {
        LocalDate startDate = LocalDate.of(2023, 7, 15);
        LocalDate endDate = LocalDate.of(2023, 7, 20);
        int bikeQuantity = 3;

        when(bikeService.areBikesAvailable(startDate, endDate, bikeQuantity)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/bikes/checkAvailability")
                        .param("startDate", "2023-07-15")
                        .param("endDate", "2023-07-20")
                        .param("bikeQuantity", "3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Bikes are available."));
    }

    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testCheckAvailability_WithInvalidInput_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bikes/checkAvailability")
                        .param("startDate", "")
                        .param("endDate", "")
                        .param("bikeQuantity", "3"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testGetAllBikes_ReturnsListOfBikeDtos() throws Exception {
        BikeDto bikeDto1 = new BikeDto();
        BikeDto bikeDto2 = new BikeDto();
        List<BikeDto> bikeDtos = Arrays.asList(bikeDto1, bikeDto2);

        when(bikeService.getAllBikes()).thenReturn(bikeDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/bikes/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testGetBikeById_WithValidId_ReturnsBike() throws Exception {
        Long bikeId = 1L;
        Bike bike = new Bike();

        when(bikeService.getBikeById(bikeId)).thenReturn(bike);

        mockMvc.perform(MockMvcRequestBuilders.get("/bikes/{id}", bikeId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testAddBike_WithValidInput_ReturnsCreated() throws Exception {
        BikeInputDto bikeInputDto = new BikeInputDto();
        bikeInputDto.setBrand("Test Brand");
        bikeInputDto.setRegistrationNo(125556L);
        bikeInputDto.setIsAvailable(true);

        when(bikeService.addBike(any(BikeInputDto.class))).thenReturn(new BikeDto());

        mockMvc.perform(MockMvcRequestBuilders.post("/bikes/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Test Brand\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }



    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testUpdateBike_WithValidInput_ReturnsBikeDto() throws Exception {
        Long bikeId = 1L;
        BikeInputDto updatedBikeInputDto = new BikeInputDto();
        updatedBikeInputDto.setBrand("Updated Brand");

        when(bikeService.partialUpdateBike(bikeId, updatedBikeInputDto)).thenReturn(new BikeDto());

        mockMvc.perform(MockMvcRequestBuilders.patch("/bikes/{id}", bikeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"brand\":\"Updated Brand\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles="ADMIN")
    void testDeleteBike_WithValidId_ReturnsNoContent() throws Exception {
        Long bikeId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/bikes/{id}", bikeId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());


        verify(bikeService).deleteBike(bikeId);
    }
}