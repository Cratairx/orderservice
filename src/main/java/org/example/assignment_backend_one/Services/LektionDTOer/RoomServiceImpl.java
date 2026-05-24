package org.example.assignment_backend_one.Services.LektionDTOer;

import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoomServiceImpl implements ServiceRooms
{
    private final RoomRepository roomRepository;


     public RoomServiceImpl(RoomRepository roomRepository) {
     this.roomRepository = roomRepository;
  }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Room saveRoom(Room room) {
       return roomRepository.save(room);
    }

    @Override
    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    public boolean saveRoom(Long id) {
        return  roomRepository.existsById(id);
    }


}
