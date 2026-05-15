package org.example.assignment_backend_one.Controllers;

import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/booking")
    public String booking() {
        return "Booking";
    }

    @PostMapping("/booking")
    public String booking(@RequestParam Long customerId) {
        bookingService.createBooking(customerId);
        return "Index";
    }
}
