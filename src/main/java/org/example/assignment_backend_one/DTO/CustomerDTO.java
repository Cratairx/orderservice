package org.example.assignment_backend_one.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assignment_backend_one.Models.Booking;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private List<Booking> bookings;
}
