package org.example.assignment_backend_one.Services.impl;

import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void getAllRoomsReturnsAllRooms() {
        Room room1 = new Room();
        Room room2 = new Room();
        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));

        List<Room> result = roomService.getAllRooms();

        assertThat(result).hasSize(2);
        verify(roomRepository).findAll();
    }

    @Test
    void getRoomByIdWhenExistsReturnsRoom() {
        Room room = new Room();
        room.setId(1L);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getRoomByIdWhenNotFoundThrowsException() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.getRoomById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Room not found");
    }

    @Test
    void deleteRoomWhenExistsReturnsTrue() {
        when(roomRepository.existsById(1L)).thenReturn(true);

        boolean result = roomService.deleteRoom(1L);

        assertThat(result).isTrue();
        verify(roomRepository).deleteById(1L);
    }

    @Test
    void deleteRoomWhenNotExistsReturnsFalse() {
        when(roomRepository.existsById(1L)).thenReturn(false);

        boolean result = roomService.deleteRoom(1L);

        assertThat(result).isFalse();
        verify(roomRepository, never()).deleteById(anyLong());
    }
}