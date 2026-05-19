package org.example.assignment_backend_one.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assignment_backend_one.Controllers.CustomerController;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class
Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long BookingId;

    // eventuellt ska denna ändras bara till @joincolum
    
    @OneToOne(mappedBy = "booking")
    private Customer customer;
}
