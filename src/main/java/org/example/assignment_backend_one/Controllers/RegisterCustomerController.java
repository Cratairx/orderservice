package org.example.assignment_backend_one.Controllers;
import jakarta.servlet.http.HttpSession;
import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.Services.LektionDTOer.CustomerImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterCustomerController {
    private final CustomerImpl registerService;

    public RegisterCustomerController(CustomerImpl registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
    @GetMapping("/register")
    public String register() {
            return "register";
    }

    // @RequestBody CustomerDTO customer
    @PostMapping("/register")
    public String register(@RequestBody CustomerDTO customer, HttpSession session, Model model) {
        boolean result = registerService.register(customer.getFirstName(), customer.getLastName(), customer.getEmail());
        System.out.println("Customer:  " + customer);
        if (result) {
            session.setAttribute("firstName", customer.getFirstName());
            session.setAttribute("lastName", customer.getLastName());
            session.setAttribute("email", customer.getEmail());
            return "redirect:/index";
        }
        model.addAttribute("error","Failed to register customer");

        return "register";

    }


    }

