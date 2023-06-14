package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.inputs.CustomerInputDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return DtoMapper.mapCustomerListToDtoList(customers);
    }

    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + id));
        return DtoMapper.mapCustomerToDto(customer);
    }

    public CustomerDto createCustomer(CustomerInputDto customerInputDto) {
        Customer customer = DtoMapper.mapDtoToCustomer(customerInputDto);
        Customer savedCustomer = customerRepository.save(customer);
        return DtoMapper.mapCustomerToDto(savedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RecordNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public CustomerDto updateCustomer(Long id, CustomerInputDto newCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + id));

        // Update the customer properties
        existingCustomer.setFirstName(newCustomer.getFirstName());
        existingCustomer.setLastName(newCustomer.getLastName());
        existingCustomer.setPhoneNo(newCustomer.getPhoneNo());
        existingCustomer.setEmail(newCustomer.getEmail());
        existingCustomer.setAddress(newCustomer.getAddress());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return DtoMapper.mapCustomerToDto(updatedCustomer);
    }
}
