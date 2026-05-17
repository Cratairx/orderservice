package org.example.assignment_backend_one.Controllers;

import jakarta.servlet.http.HttpSession;
import org.example.assignment_backend_one.Services.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterCustomerController {
    private final RegisterService registerService;

    public RegisterCustomerController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/register")
    public String register(HttpSession session, Model model) {
 // osäkert på om vi måste hämta allt eller inte men tänker att det kan vara snyggt.
        model.addAttribute("firstName");
        model.addAttribute("lastName");
        model.addAttribute("email");
        session.getAttribute("firstName");
        session.getAttribute("lastName");
        session.getAttribute("email");

        return "register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           HttpSession session,
                           Model model) {
        boolean result = registerService.register(firstName,lastName,email);
        if (result) {
            session.setAttribute("firstName", firstName);
            session.setAttribute("lastName", lastName);
            session.setAttribute("email", email);
            return "redirect:/index";
        }
        model.addAttribute("error","Failed to register customer");

        return "register";
    }
}
