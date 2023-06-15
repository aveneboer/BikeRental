package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.inputs.CustomerInputDto;
import nl.anouk.bikerental.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> dtos = customerService.getAllCustomers();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/customers")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerInputDto customerInputDto) {
        CustomerDto dto = customerService.createCustomer(customerInputDto);
        return ResponseEntity.created(null).body(dto);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerInputDto newCustomer) {
        CustomerDto dto = customerService.updateCustomer(id, newCustomer);
        return ResponseEntity.ok().body(dto);
    }
}