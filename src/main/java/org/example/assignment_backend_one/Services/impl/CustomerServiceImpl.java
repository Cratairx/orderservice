package org.example.assignment_backend_one.Services.impl;

import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.DTO.DetailedCustomerDTO;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.example.assignment_backend_one.Services.CustomerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final BookingService bookingService;

    @Override
    public CustomerDTO customerToCustomerDTO(Customer c) {
        return CustomerDTO.builder().id(c.getId()).firstName(c.getFirstName()).lastName(c.getLastName()).build();
    }

    @Override
    public DetailedCustomerDTO customerToDetailedCustomerDTO(Customer c) {
        return DetailedCustomerDTO.builder().id(c.getId())
                .firstName(c.getFirstName()).lastName(c.getLastName())
                .email(c.getEmail()).bookings(c.getBookings().stream()
                        .map(bookingService::bookingToBookingDTO).toList()).build();
    }

    @Override
    public List<DetailedCustomerDTO> getAllDetailedCustomersDto() {
        return customerRepository.findAll().stream()
                .map(this::customerToDetailedCustomerDTO)
                .toList();
    }

    @Override
    public boolean register(Long id, String firstName, String lastName, String email) {
        if( firstName == null || lastName == null || email == null ){
            return false;
        }
        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
            return false;
        }

        if (customerRepository.findByEmail(email).isPresent()){
            return false;
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customerRepository.save(customer);

        return true;
    }

    @Override
    public boolean deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            return false;
        }
        if (customer.getBookings() != null && !customer.getBookings().isEmpty()) {
            return false;
        }

        customerRepository.deleteById(id);
        return true;

    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public void updateCustomer(Long id, String firstname, String lastname, String email) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setFirstName(firstname);
            customer.setLastName(lastname);
            customer.setEmail(email);
            customerRepository.save(customer);
        }
    }

}
