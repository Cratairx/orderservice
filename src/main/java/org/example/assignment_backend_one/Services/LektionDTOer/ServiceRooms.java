package org.example.assignment_backend_one.Services.LektionDTOer;

import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.RoomRepository;

import java.util.List;

public interface ServiceRooms {


    public List<Room> getAllRooms();
    public Room saveRoom(Room room);
    public boolean deleteRoom(Long id);

}
