package org.example.assignment_backend_one.Services.impl;

import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.DTO.BookingDTO;
import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

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

}
