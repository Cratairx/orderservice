package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;

  }

  public Customer getCustomerById(Long id) {
    return customerRepository.findById(id).orElse(null);
  }
}
