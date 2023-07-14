package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testGetCustomerByLastName_WithExistingLastName_ReturnsCustomerDto() {
        // Arrange
        String lastName = "Henk";
        Customer customer = new Customer();
        customer.setLastName(lastName);
        customerRepository.save(customer);

        // Act
        CustomerDto customerDto = customerService.getCustomerByLastName(lastName);

        // Assert
        assertNotNull(customerDto);
        assertEquals(lastName, customerDto.getLastName());
    }

    @Test
    public void testGetCustomerByLastName_WithNonExistingLastName_ThrowsRecordNotFoundException() {
        // Arrange
        String lastName = "Doe";

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> {
            customerService.getCustomerByLastName(lastName);
        });
    }
}