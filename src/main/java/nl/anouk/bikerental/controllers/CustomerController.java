package nl.anouk.bikerental.controllers;

import jakarta.validation.Valid;
import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.inputs.CustomerInputDto;
import nl.anouk.bikerental.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RequestMapping("/customers")
@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> dtos = customerService.getAllCustomers();
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable("id") Long id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok().body(customer);
    }

    @PostMapping("/addcustomer")
    public ResponseEntity<Object> createCustomer(@Valid @RequestBody CustomerInputDto customerInputDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField() + ": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            CustomerDto createdCustomer = customerService.createCustomer(customerInputDto);

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().path("/" + createdCustomer.getCustomerId()).toUriString());

            return ResponseEntity.created(uri).body(createdCustomer);
        }
    }
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerInputDto newCustomer) {
        CustomerDto dto = customerService.updateCustomer(id, newCustomer);
        return ResponseEntity.ok().body(dto);
    }
    @PatchMapping("/customers/{id}")
    public ResponseEntity<Object> partialUpdateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerInputDto newCustomer) {
        CustomerDto dto = customerService.partialUpdateCustomer(id, newCustomer);
        return ResponseEntity.ok().body(dto);
    }
}

