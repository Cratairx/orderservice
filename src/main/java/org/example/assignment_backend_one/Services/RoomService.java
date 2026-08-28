package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.Room;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    List<Room> getAllRooms();

    Room saveRoom(Room room);

    boolean deleteRoom(Long id);

    Room getRoomById(Long id);

    boolean saveRoom(Long id);

    List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate);

}