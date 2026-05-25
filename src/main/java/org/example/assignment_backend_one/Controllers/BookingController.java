package org.example.assignment_backend_one.Controllers;


import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.Services.BookingService;
import org.example.assignment_backend_one.Services.CustomerService;
import org.example.assignment_backend_one.Services.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final CustomerService customerService;
    private final RoomService roomService;

    @GetMapping("/new")
    public String showBookingForm(Model model) {
        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "Booking";
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
            redirectAttributes.addFlashAttribute("success", "Booking created!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Room is already booked for those dates.");
        }
        return "redirect:/bookings/new";
    }

    @GetMapping
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "Booking";
    }

    @GetMapping("/available")
    public String showAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                model.addAttribute("error", "End date cannot be before start date.");
            } else {
                model.addAttribute("rooms", bookingService.getAvailableRooms(startDate, endDate));
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
            }
        }

        return "available-rooms";
    }

/*

    @GetMapping("/editbooking/{id}")
    public String showEditBookingForm(@PathVariable Long id, Model model) {
        Booking booking = bookingService.getBookingById(id);
        model.addAttribute("booking", booking);
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("rooms", roomRepository.findAll());
        return "editBooking";
    }

    @PostMapping("/editbooking/{id}")
    public String editBooking(
            @PathVariable Long id,
            @RequestParam Long customerId,
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirectAttributes) {

        Customer customer = customerRepository.findById(customerId).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        if (customer == null || room == null) {
            redirectAttributes.addFlashAttribute("error", "Kund eller rum hittades inte.");
            return "redirect:/bookings/editbooking/" + id;
        }

        Booking booking = bookingService.getBookingById(id);
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        bookingService.saveBooking(booking);

        redirectAttributes.addFlashAttribute("success", "Bokning ändrad!");
        return "redirect:/bookings";
    }

 */
}