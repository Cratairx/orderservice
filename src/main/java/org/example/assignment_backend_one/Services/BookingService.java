package org.example.assignment_backend_one.Services;

import java.time.LocalDate;

public interface BookingService {

    boolean createBooking(Long customerId, Long roomId, LocalDate startDate, LocalDate endDate);

    boolean updateBooking(Long id, Long roomId, LocalDate startDate, LocalDate endDate);

    boolean deleteBooking(Long id);
}