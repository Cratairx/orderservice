package org.example.assignment_backend_one.Controllers;

import org.example.assignment_backend_one.ENUMS.RoomType;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Services.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    // Konstruktorinjektion
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Visar formulär för att skapa ett nytt rum
    @GetMapping("/new")
    public String showRoomForm(Model model) {
        // Skickar med rumtyper så Thymeleaf kan bygga en dropdown
        model.addAttribute("roomTypes", RoomType.values());
        return "room-form";
    }

    // Tar emot formuläret och skapar rummet
    @PostMapping("/new")
    public String createRoom(
            @RequestParam String roomNumber,
            @RequestParam RoomType roomType,
            RedirectAttributes redirectAttributes) {

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        roomService.saveRoom(room);

        redirectAttributes.addFlashAttribute("success", "Rum " + roomNumber + " skapades!");
        return "redirect:/rooms/new";
    }

    // Visar alla rum
    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }

    // Tar bort ett rum och redirectar tillbaka till rumlistan
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean success = roomService.deleteRoom(id);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Rummet togs bort.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Rummet hittades inte.");
        }
        return "redirect:/rooms";
    }
}