package org.example.assignment_backend_one.Controllers;
import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.DTO.DetailedBookingDTO;
import org.example.assignment_backend_one.DTO.SmallBookingDTO;
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
    public String showBookingForm(
            Model model,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
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
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "bookings";
    }

    @GetMapping("/available")
    public String showAvailableRooms(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "1") int guests,
            Model model) {

        if (startDate != null && endDate != null) {
            if (endDate.isBefore(startDate)) {
                model.addAttribute("error", "End date cannot be before start date.");
            } else {
                model.addAttribute("rooms", bookingService.getAvailableRooms(startDate, endDate, guests));
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("guests", guests);
            }
        }

        return "availableRooms";
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
    // delete booking funktion ska finnas. @{/bookings/deleteBooking/{id}(id=${booking.id})}
    @PostMapping("/deletebooking/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        boolean success = bookingService.deleteBooking(id);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Rummet togs bort.");

        } else {
            redirectAttributes.addFlashAttribute("error", "Rummet hittades inte.");
        }
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "/bookings";
    }


}