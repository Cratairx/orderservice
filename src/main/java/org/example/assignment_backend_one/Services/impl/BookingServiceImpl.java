package org.example.assignment_backend_one.Services.impl;

import org.example.assignment_backend_one.Client.CustomerClient;
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
    public BookingServiceImpl(BookingRepository bookingRepository, RoomRepository roomRepository, CustomerClient customerClient) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.customerClient = customerClient;
    }

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final CustomerClient customerClient;

    @Override
    public boolean createBooking(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {

        if (!customerClient.customerExists(customerId)) return false;

        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) return false;

        List<Room> availableRooms = roomRepository.findAvailableRooms(startDate, endDate);
        if (!availableRooms.contains(room)) return false;

        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCustomerID(customerId);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public boolean updateBooking(Long id, Long roomId, LocalDate startDate, LocalDate endDate) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

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

