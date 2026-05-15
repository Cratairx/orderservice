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

  public void deleteCustomer(Long id) {
      Customer customer = customerRepository.findById(id).orElse(null);

      if (customer.getBooking() != null) {
          throw new RuntimeException("Customer has a booking");
      }

      customerRepository.deleteById(id);
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
}
