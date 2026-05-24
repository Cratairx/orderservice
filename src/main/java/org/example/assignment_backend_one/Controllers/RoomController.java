package org.example.assignment_backend_one.Controllers;

import jakarta.servlet.http.HttpSession;
import org.example.assignment_backend_one.DTO.DetailedRoomDTO;
import org.example.assignment_backend_one.ENUMS.RoomType;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Services.impl.RoomServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    public RoomController( RoomServiceImpl roomService) {
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
    // @ModelAttribute CustomerDTO customer
    // rummet skapas inte atm. FIXA!!!
    @PostMapping("/new")
    public String createRoom(@ModelAttribute DetailedRoomDTO room, HttpSession session, RedirectAttributes redirectAttributes) {

      Room newRoom = new Room();
      newRoom.setRoomNumber(room.getRoomNumber());
      newRoom.setRoomType(room.getRoomType());
      Room savedRoom = roomService.saveRoom(newRoom);
      if (savedRoom != null) {
          redirectAttributes.addFlashAttribute("message", "Room has been saved successfully");
      }else{
          redirectAttributes.addFlashAttribute("error", "Room could not be saved");
      }
        return "redirect:/rooms/new";


    }
    /*@PostMapping("/new")
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
    }*/

    // Visar alla rum
    @GetMapping
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "rooms";
    }
    @GetMapping("/editroom/{id}")
    public String showRoomForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        model.addAttribute("roomTypes", RoomType.values());
        return "editroom";
    }
    @PostMapping("editroom/{id}")
    public String editRoom(@PathVariable Long id,
                           @RequestParam RoomType roomType,
                           @RequestParam String roomNumber ) {

        Room room = roomService.getRoomById(id);
        room.setRoomNumber(roomNumber);
        room.setRoomType(roomType);
        roomService.saveRoom(room);
        return "redirect:/rooms";
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