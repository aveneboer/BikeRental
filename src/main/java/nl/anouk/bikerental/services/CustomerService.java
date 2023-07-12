package nl.anouk.bikerental.services;

import nl.anouk.bikerental.dtos.CustomerDto;
import nl.anouk.bikerental.exceptions.RecordNotFoundException;
import nl.anouk.bikerental.inputs.CustomerInputDto;
import nl.anouk.bikerental.models.Customer;
import nl.anouk.bikerental.models.DtoMapper;
import nl.anouk.bikerental.repositories.CustomerRepository;
import nl.anouk.bikerental.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    public CustomerService(CustomerRepository customerRepository, ReservationRepository reservationRepository) {
        this.customerRepository = customerRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return DtoMapper.mapCustomerListToDtoList(customers);
    }


    public CustomerDto getCustomerByLastName(String lastName) {
        Customer customer = customerRepository.getByLastNameIgnoreCase(lastName);
        if (customer == null) {
            throw new RecordNotFoundException("Customer not found with last name: " + lastName);
        }
        return DtoMapper.mapCustomerToDto(customer, true);
    }


    public CustomerDto createCustomer(CustomerInputDto customerInputDto) {
        Customer customer = DtoMapper.mapCustomerInputDtoToEntity(customerInputDto);
        Customer savedCustomer = customerRepository.save(customer);
        return DtoMapper.mapCustomerToDto(savedCustomer, false);
    }


    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RecordNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    public CustomerDto updateCustomer(Long id, CustomerInputDto newCustomer) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + id));
        customer.setFirstName(newCustomer.getFirstName());
        customer.setLastName(newCustomer.getLastName());
        customer.setPhoneNo(newCustomer.getPhoneNo());
        customer.setEmail(newCustomer.getEmail());
        customer.setAddress(newCustomer.getAddress());
        Customer updatedCustomer = customerRepository.save(customer);
        CustomerDto dto = DtoMapper.mapCustomerToDto(updatedCustomer, false);

        return dto;
    }


    public CustomerDto partialUpdateCustomer(Long id, CustomerInputDto newCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + id));

        existingCustomer.setFirstName(newCustomer.getFirstName());
        existingCustomer.setLastName(newCustomer.getLastName());
        existingCustomer.setPhoneNo(newCustomer.getPhoneNo());
        existingCustomer.setEmail(newCustomer.getEmail());
        existingCustomer.setAddress(newCustomer.getAddress());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return DtoMapper.mapCustomerToDto(updatedCustomer, false);
    }


    public void linkUserToCustomer(String username, String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        customer.setUser(username);
        customerRepository.save(customer);
    }

    }

