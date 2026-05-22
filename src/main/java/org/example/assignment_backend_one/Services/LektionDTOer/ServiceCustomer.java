package org.example.assignment_backend_one.Services.LektionDTOer;

import org.example.assignment_backend_one.Models.Customer;

import java.util.List;

public interface ServiceCustomer {
    public Customer getCustomerById(Long id);
    public void updateCustomer(Long id, String firstname, String lastname, String email);
    public boolean deleteCustomer(Long id);
    public List<Customer> getAllCustomers();

}
