package nl.anouk.bikerental.controllers;

import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.inputs.CustomerInputDto;
import nl.anouk.bikerental.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testGetAllCustomers_ReturnsCustomerDtoList() {
        // Arrange
        List<CustomerDto> customerDtoList = Arrays.asList(
                new CustomerDto(1L, "John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null),
                new CustomerDto(2L, "Jane", "Smith", "987654321", "jane.smith@example.com", "Address 2", null, null)
                );

        given(customerService.getAllCustomers()).willReturn(customerDtoList);

        // Act
        ResponseEntity<List<CustomerDto>> response = customerController.getAllCustomers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDtoList, response.getBody());
        verify(customerService).getAllCustomers();
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testGetCustomer_ValidLastName_ReturnsCustomerDto() {
        // Arrange
        String lastName = "Doe";
        CustomerDto customerDto = new CustomerDto(1L, "John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null);

        given(customerService.getCustomerByLastName(lastName)).willReturn(customerDto);

        // Act
        ResponseEntity<CustomerDto> response = customerController.getCustomer(lastName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDto, response.getBody());
        verify(customerService).getCustomerByLastName(lastName);
    }

    @Test
    @WithMockUser(username="testuser", roles="USER")
    void testCreateCustomer_ValidInput_ReturnsCreated() {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        CustomerInputDto customerInputDto = new CustomerInputDto("John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null);
        CustomerDto createdCustomerDto = new CustomerDto(2L, "Jane", "Smith", "987654321", "jane.smith@example.com", "Address 2", null, null);

        given(customerService.createCustomer(customerInputDto)).willReturn(createdCustomerDto);

        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasErrors()).willReturn(false);

        // Act
        ResponseEntity<Object> response = customerController.createCustomer(customerInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdCustomerDto, response.getBody());
        verify(customerService).createCustomer(customerInputDto);

        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    @WithMockUser(username="testuser", roles="USER")
    void testCreateCustomer_ValidationErrors_ReturnsBadRequest() {
        // Arrange
        CustomerInputDto customerInputDto = new CustomerInputDto("John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null);

        BindingResult bindingResult = mock(BindingResult.class);
        given(bindingResult.hasFieldErrors()).willReturn(true);
        given(bindingResult.getFieldErrors()).willReturn(Arrays.asList(
                new FieldError("customerInputDto", "firstName", "First name is required."),
                new FieldError("customerInputDto", "lastName", "Last name is required.")
        ));

        // Act
        ResponseEntity<Object> response = customerController.createCustomer(customerInputDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("firstName: First name is required.\nlastName: Last name is required.\n", response.getBody());
        verify(customerService, never()).createCustomer(any(CustomerInputDto.class));
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testDeleteCustomer_ValidId_ReturnsNoContent() {
        // Arrange
        Long customerId = 1L;

        // Act
        ResponseEntity<Object> response = customerController.deleteCustomer(customerId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService).deleteCustomer(customerId);
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testUpdateCustomer_ValidInput_ReturnsCustomerDto() {
        // Arrange
        Long customerId = 1L;
        CustomerInputDto newCustomerInputDto = new CustomerInputDto("John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null);
        CustomerDto updatedCustomerDto = new CustomerDto(2L, "Jane", "Smith", "987654321", "jane.smith@example.com", "Address 2", null, null);

        when(customerService.updateCustomer(customerId, newCustomerInputDto)).thenReturn(updatedCustomerDto);

        // Act
        ResponseEntity<Object> response = customerController.updateCustomer(customerId, newCustomerInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomerDto, response.getBody());
        verify(customerService).updateCustomer(customerId, newCustomerInputDto);
    }

    @Test
    @WithMockUser(username="testuser", roles="ADMIN")
    void testPartialUpdateCustomer_ValidInput_ReturnsCustomerDto() {
        // Arrange
        Long customerId = 1L;
        CustomerInputDto newCustomerInputDto = new CustomerInputDto("John", "Doe", "123456789", "john.doe@example.com", "Address 1", null, null);
        CustomerDto updatedCustomerDto = new CustomerDto(2L, "Jane", "Smith", "987654321", "jane.smith@example.com", "Address 2", null, null);

        when(customerService.partialUpdateCustomer(customerId, newCustomerInputDto)).thenReturn(updatedCustomerDto);

        // Act
        ResponseEntity<Object> response = customerController.partialUpdateCustomer(customerId, newCustomerInputDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCustomerDto, response.getBody());
        verify(customerService).partialUpdateCustomer(customerId, newCustomerInputDto);
    }
}