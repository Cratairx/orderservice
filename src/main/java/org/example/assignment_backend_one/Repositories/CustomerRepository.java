package org.example.assignment_backend_one.Repositories;

import org.example.assignment_backend_one.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Object> findByEmail(String email);

    Customer getCustomersById(Long id);
}
