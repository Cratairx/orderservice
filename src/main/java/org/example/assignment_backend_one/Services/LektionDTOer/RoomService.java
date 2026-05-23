package org.example.assignment_backend_one.Services.LektionDTOer;

import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;

import java.util.List;

public class RoomService implements ServiceRooms
{
    private final RoomRepository roomRepository;


     public RoomService(RoomRepository roomRepository) {
     this.roomRepository = roomRepository;
  }

    @Override
    public List<Room> getAllRooms() {
        return List.of();
    }

    @Override
    public Room saveRoom(Room room) {
        return null;
    }

    @Override
    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
