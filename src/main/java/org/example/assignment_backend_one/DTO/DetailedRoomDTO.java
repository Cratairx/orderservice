package org.example.assignment_backend_one.DTO;
import org.example.assignment_backend_one.ENUMS.RoomType;


public class DetailedRoomDTO {

        private Long id;
        private String roomNumber;
        private RoomType roomType; // SINGLE eller DOUBLE

    public DetailedRoomDTO(Long id, String roomNumber, RoomType roomType) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
