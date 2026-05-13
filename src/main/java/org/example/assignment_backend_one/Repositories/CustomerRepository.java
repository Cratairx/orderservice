package org.example.assignment_backend_one.Repositories;

import org.example.assignment_backend_one.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
