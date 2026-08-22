package org.example.assignment_backend_one.Services.impl;
import org.example.assignment_backend_one.DTO.BookingDTO;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.DTO.RoomDTO;
import org.example.assignment_backend_one.ENUMS.RoomType;
import org.example.assignment_backend_one.Models.Booking;

import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.BookingRepository;

import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class BookingServiceImpl implements BookingService {
    public BookingServiceImpl(BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;

        this.roomRepository = roomRepository;
    }

    private final BookingRepository bookingRepository;

    private final RoomRepository roomRepository;


    @Override
    public BookingDTO bookingToBookingDTO(Booking booking) {
       /* return new BookingDTO(
                booking.getId(),
                new CustomerDTO(
                        booking.getCustomer().getId(),

                booking.getRoom(),
                booking.getStartDate(),
                booking.getEndDate()); */
    }


    @Override
    public DetailedBookingDTO bookingToDetailedDTO(Booking booking){
       /* return new DetailedBookingDTO(
                booking.getId(),
                new CustomerDTO(
                booking.getCustomer().getId(),
                booking.getRoom(),
                booking.getStartDate(),
                booking.getEndDate());*/
    }

    @Override
    public boolean createBooking(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        if (customer == null || room == null) return false;

        List<Room> availableRooms = roomRepository.findAvailableRooms(startDate, endDate);
        if (!availableRooms.contains(room)) return false;

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public List<DetailedBookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::bookingToDetailedDTO).collect(Collectors.toList());
    }

    @Override
    public DetailedBookingDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        return bookingToDetailedDTO(booking);
    }

    @Override
    public List<RoomDTO> getAvailableRooms(LocalDate startDate, LocalDate endDate, int guests) {
        return roomRepository.findAvailableRooms(startDate, endDate).stream()
                .filter(room -> guests < 2 || room.getRoomType() == RoomType.DOUBLE)
                .map(this::toRoomDTO)
                .toList();
    }
    // fråga yahya om hur man fixar en fråga nu när customerrepo inte är lokalt längre

    @Override
    public boolean updateBooking(Long id, Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        if (booking == null || customer == null || room == null) return false;

        boolean hasConflict = bookingRepository.existsOverlappingBooking(roomId, startDate, endDate, id);
        if (hasConflict) return false;

        booking.setCustomer(customer);
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
    private RoomDTO toRoomDTO(Room room) {
    return new RoomDTO( room.getId(),room.getRoomNumber(),room.getRoomType());

    }




}
