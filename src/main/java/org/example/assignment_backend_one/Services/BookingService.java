package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.BookingRepository;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
    }

    public void createBooking(Long customerId) {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        assert customer != null;
        if (customer.getBooking() != null) {
            throw new RuntimeException("Customer already has a booking");
        }

        Booking booking = new Booking();
        Booking savedBooking = bookingRepository.save(booking);

        customer.setBooking(savedBooking);
        customerRepository.save(customer);

    }
}
