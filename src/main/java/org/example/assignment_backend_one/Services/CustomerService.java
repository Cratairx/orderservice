package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.DTO.DetailedCustomerDTO;
import org.example.assignment_backend_one.Models.Customer;
import java.util.List;

public interface CustomerService {

    CustomerDTO customerToCustomerDTO(Customer c);

    DetailedCustomerDTO customerToDetailedCustomerDTO(Customer c);

    List<DetailedCustomerDTO> getAllDetailedCustomersDto();

    boolean register(Long id, String firstName, String lastName, String email);

    boolean deleteCustomer(Long id);

    Customer getCustomerById(Long id);

    boolean updateCustomer(Long id, String firstname, String lastname, String email);

}
