package org.example.assignment_backend_one.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class
Customer {
    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;


    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings;

}
