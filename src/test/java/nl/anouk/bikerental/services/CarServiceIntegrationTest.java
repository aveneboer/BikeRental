package nl.anouk.bikerental.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CarServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    public void testGetAllCars() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/cars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].model").value("Toyota Highlander"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].passenger").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].model").value("Toyota RAV4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].passenger").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2));
    }


    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    public void testAddCar() throws Exception {
        String carJson = "{ \"model\": \"Hatchback\", \"passenger\": 4, \"isAvailable\": true, \"quantity\": 2 }";

        mockMvc.perform(MockMvcRequestBuilders.post("/cars/addCar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value("Hatchback"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passenger").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAvailable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2));
    }


}
