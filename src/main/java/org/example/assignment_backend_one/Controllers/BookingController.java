package org.example.assignment_backend_one.Controllers;


import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.DTO.SmallBookingDTO;
import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Models.Room;
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



    @GetMapping("/editbooking/{id}")
    public String showEditBookingForm(@PathVariable Long id, Model model) {
        DetailedBookingDTO booking = bookingService.getBookingById(id);

       SmallBookingDTO form = SmallBookingDTO.builder()
                .id(booking.getId())
                .customerId(booking.getCustomer().getId())
                .roomId(booking.getRoom().getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();

        model.addAttribute("bookingForm", form);
        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "editBooking";
    }

    @PostMapping("/editbooking")
    public String editBooking(@ModelAttribute SmallBookingDTO bookingForm,
                              RedirectAttributes redirectAttributes) {

        boolean success = bookingService.updateBooking(
                bookingForm.getId(),
                bookingForm.getCustomerId(),
                bookingForm.getRoomId(),
                bookingForm.getStartDate(),
                bookingForm.getEndDate());

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Booking updated!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Could not update booking.");
        }
        return "redirect:/bookings";
    }


}