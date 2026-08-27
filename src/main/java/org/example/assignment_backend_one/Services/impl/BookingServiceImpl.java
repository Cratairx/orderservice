package org.example.assignment_backend_one.Services.impl;

import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.BookingRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service

public class BookingServiceImpl implements BookingService {
    public BookingServiceImpl(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;

        this.roomRepository = roomRepository;
    }

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;

    @Override
    public boolean createBooking(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {
        Room room = roomRepository.findById(roomId).orElse(null);

        //if (customer == null || room == null) return false;

        List<Room> availableRooms = roomRepository.findAvailableRooms(startDate, endDate);
        if (!availableRooms.contains(room)) return false;

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingRepository.save(booking);
        return true;
    }

    // fråga yahya om hur man fixar en fråga nu när customerrepo inte är lokalt längre

    @Override
    public boolean updateBooking(Long id, Long roomId, LocalDate startDate, LocalDate endDate) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        //Customer customer = customerRepository.findById(customerId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        //if (booking == null || customer == null || room == null) return false;

        boolean hasConflict = bookingRepository.existsOverlappingBooking(roomId, startDate, endDate, id);
        if (hasConflict) return false;

        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
    }

}

