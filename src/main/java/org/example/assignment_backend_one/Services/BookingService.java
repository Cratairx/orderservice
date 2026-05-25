package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.DTO.BookingDTO;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.DTO.RoomDTO;
import org.example.assignment_backend_one.Models.*;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDTO bookingToBookingDTO(Booking booking);

    DetailedBookingDTO bookingToDetailedDTO(Booking booking);

    boolean createBooking(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate);

    List<DetailedBookingDTO> getAllBookings();

    DetailedBookingDTO getBookingById(Long id);

    List<RoomDTO> getAvailableRooms(LocalDate startDate, LocalDate endDate);

    boolean updateBooking(Long id, Long customerId, Long roomId, LocalDate startDate, LocalDate endDate);

}