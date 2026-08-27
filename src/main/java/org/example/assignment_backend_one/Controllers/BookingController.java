package org.example.assignment_backend_one.Controllers;

import org.example.assignment_backend_one.Services.BookingService;
import org.example.assignment_backend_one.Services.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;



@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final RoomService roomService;

    public BookingController(BookingService bookingService, RoomService roomService) {
        this.bookingService = bookingService;
        this.roomService = roomService;
    }

    @GetMapping("/new")
    public String showBookingForm(

            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return "createBooking";
    }

    @PostMapping("/new")
    public String createBooking(
            @RequestParam Long customerId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirectAttributes) {

        boolean success = bookingService.createBooking(customerId, roomId, startDate, endDate);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Bokning skapad!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Room is already booked for those dates.");
        }
        return "redirect:/bookings/new";
    }

    @GetMapping
    public String listBookings() {
        return "bookings";
    }

    @GetMapping("/available")
    public String showAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "1") int guests) {

        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {

            } else {

            }
        }

        return "availableRooms";
    }
    @GetMapping("/editbooking/{id}")
    public String showEditBookingForm(@PathVariable Long id) {

        return "editBooking";
    }

    @PostMapping("/editbooking")
    public String editBooking() {
        return "/booking";
    }

    @PostMapping("/deletebooking/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = bookingService.deleteBooking(id);

        return "/bookings";
    }


}