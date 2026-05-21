package org.example.assignment_backend_one.Controllers;

import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    // Konstruktorinjektion – Spring sätter in rätt beroenden automatiskt
    public BookingController(BookingService bookingService,
                             CustomerRepository customerRepository,
                             RoomRepository roomRepository) {
        this.bookingService = bookingService;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    // Visar bokningsformuläret med alla kunder och rum ifyllda i dropdowns
    @GetMapping("/new")
    public String showBookingForm(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        return "Booking";
    }

    // Tar emot formuläret och försöker skapa en bokning
    @PostMapping("/new")
    public String createBooking(
            @RequestParam Long customerId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirectAttributes) {

        // Hämtar kund och rum från databasen via id
        Customer customer = customerRepository.findById(customerId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        // Kontrollerar att kund och rum faktiskt finns
        if (customer == null || room == null) {
            redirectAttributes.addFlashAttribute("error", "Kund eller rum hittades inte.");
            return "redirect:/bookings/new";
        }

        // Försöker skapa bokning – returnerar false om rummet är upptaget
        boolean success = bookingService.createBooking(customer, room, startDate, endDate);

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Bokning skapad!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Rummet är redan bokat för det angivna datumintervallet.");
        }

        return "redirect:/bookings/new";
    }

    // Visar en lista över alla bokningar
    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }
}