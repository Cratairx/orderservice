package org.example.assignment_backend_one.Services.impl;

import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.DTO.BookingDTO;
import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.DTO.RoomDTO;
import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.BookingRepository;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    @Override
    public BookingDTO bookingToBookingDTO(Booking booking) {
        return BookingDTO.builder()
                .id(booking.getId())
                .customer(CustomerDTO.builder()
                        .id(booking.getCustomer().getId())
                        .firstName(booking.getCustomer().getFirstName())
                        .lastName(booking.getCustomer().getLastName())
                        .build())
                .room(booking.getRoom())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
    }

    @Override
    public DetailedBookingDTO bookingToDetailedDTO(Booking booking) {
        return DetailedBookingDTO.builder().id(booking.getId())
                .customer(CustomerDTO.builder()
                        .id(booking.getCustomer().getId())
                        .firstName(booking.getCustomer().getFirstName())
                        .lastName(booking.getCustomer().getLastName())
                        .build())
                .room(booking.getRoom())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
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
    public List<RoomDTO> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        return roomRepository.findAvailableRooms(startDate, endDate).stream()
                .map(this::toRoomDTO)
                .toList();
    }

    @Override
    public boolean updateBooking(Long id, Long customerId, Long roomId, LocalDate startDate, LocalDate endDate) {
        return false;
    }

    private RoomDTO toRoomDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .build();
    }

}
