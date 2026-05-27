package org.example.assignment_backend_one.Services.impl;

import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.BookingRepository;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingWhenRoomAvailableSavesAndReturnsTrue() {
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2026, 6, 5);

        Customer customer = new Customer();
        customer.setId(1L);
        Room room = new Room();
        room.setId(2L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(roomRepository.findAvailableRooms(start, end)).thenReturn(List.of(room));

        boolean result = bookingService.createBooking(1L, 2L, start, end);

        assertThat(result).isTrue();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBookingWhenCustomerMissingReturnsFalse() {
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2026, 6, 5);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        when(roomRepository.findById(2L)).thenReturn(Optional.of(new Room()));

        boolean result = bookingService.createBooking(1L, 2L, start, end);

        assertThat(result).isFalse();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBookingWhenRoomNotAvailableReturnsFalse() {
        LocalDate start = LocalDate.of(2026, 6, 1);
        LocalDate end = LocalDate.of(2026, 6, 5);

        Customer customer = new Customer();
        Room room = new Room();
        room.setId(2L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(roomRepository.findById(2L)).thenReturn(Optional.of(room));
        when(roomRepository.findAvailableRooms(start, end)).thenReturn(List.of()); // empty

        boolean result = bookingService.createBooking(1L, 2L, start, end);

        assertThat(result).isFalse();
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void deleteBookingWhenExistsReturnsTrue() {
        when(bookingRepository.existsById(5L)).thenReturn(true);

        boolean result = bookingService.deleteBooking(5L);

        assertThat(result).isTrue();
        verify(bookingRepository).deleteById(5L);
    }
}