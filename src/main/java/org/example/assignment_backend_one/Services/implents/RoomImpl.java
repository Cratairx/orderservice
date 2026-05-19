package org.example.assignment_backend_one.Services.implents;

import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
  public class RoomImpl {
    // interface till DTOER

    private RoomRepository roomRepository;
    public RoomImpl(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
}


}
