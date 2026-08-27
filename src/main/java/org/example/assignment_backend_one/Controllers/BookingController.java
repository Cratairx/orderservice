package org.example.assignment_backend_one.Controllers;

import org.example.assignment_backend_one.Services.BookingService;
import org.example.assignment_backend_one.Services.RoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
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
            Model model,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

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
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                model.addAttribute("guests", guests);
            }
        }

        return "availableRooms";
    }
    @GetMapping("/editbooking/{id}")
    public String showEditBookingForm(@PathVariable Long id, Model model) {

        model.addAttribute("rooms", roomService.getAllRooms());
        return "editBooking";
    }




    /*@GetMapping("/editbooking/{id}")
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
    }*/

    @PostMapping("/editbooking")
    public String editBooking() {
                /*
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Bokning updaterad!");
        } else {
            redirectAttributes.addFlashAttribute("error", "kan inte updatera bookning.");
        }
        */

        return "redirect:/bookings";
    }

    @PostMapping("/deletebooking/{id}")
    public String deleteBooking(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
        boolean success = bookingService.deleteBooking(id);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Bokning borttagen!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Bookning finns inte.");
        }

        return "redirect:/bookings";
    }


}