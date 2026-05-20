package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

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

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void updateCustomer(Long id, String firstname, String lastname, String email) {
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer != null) {
            customer.setFirstName(firstname);
            customer.setLastName(lastname);
            customer.setEmail(email);
            customerRepository.save(customer);
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
