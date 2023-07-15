package nl.anouk.bikerental.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerServiceIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

}