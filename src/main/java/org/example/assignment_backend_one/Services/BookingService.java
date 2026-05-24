package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.*;
import org.example.assignment_backend_one.Repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAvailableRooms(startDate, endDate);
    }

    public boolean createBooking(Customer customer, Room room, LocalDate startDate, LocalDate endDate) {
        List<Room> available = roomRepository.findAvailableRooms(startDate, endDate);
        if (!available.contains(room)) {
            return false; // rummet är redan bokat
        }
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingRepository.save(booking);
        return true;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
       return bookingRepository.findById(id)
               .orElseThrow(()-> new RuntimeException("Booking not found!! with id " + id));
    }

    public void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }
}