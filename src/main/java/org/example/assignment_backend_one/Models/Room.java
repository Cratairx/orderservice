package org.example.assignment_backend_one.Models;

import jakarta.persistence.*;
import lombok.*;
import org.example.assignment_backend_one.ENUMS.RoomType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType; // SINGLE eller DOUBLE

}