package org.example.assignment_backend_one.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.assignment_backend_one.ENUMS.RoomType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailedRoomDTO {

        private Long id;
        private String roomNumber;
        private RoomType roomType; // SINGLE eller DOUBLE


}
