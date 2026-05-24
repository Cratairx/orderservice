package org.example.assignment_backend_one.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assignment_backend_one.Models.Room;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {

    private Long id;
    private CustomerDTO customer;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;

}
