package org.example.assignment_backend_one.Controllers;

import lombok.RequiredArgsConstructor;
import org.example.assignment_backend_one.Services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor

public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/index")
    public String index() {
        return "Index";
    }

    @GetMapping("/")
    public String home() {
        return "Index";
    }

    @GetMapping("/customer")
    public String customer() {
        return "customer";
    }

    @RequestMapping("/deletecustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model) {

        boolean result = customerService.deleteCustomer(id);

        if (result) {
            model.addAttribute("customer", customerService.getCustomerById(id));
            return "redirect:/allcustomers";
        }

        model.addAttribute("error","Failed to delete customer");
        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());

        return "/allcustomers";

    }

    @GetMapping("/allcustomers")
    public String allcustomers(Model model) {
        model.addAttribute("customers", customerService.getAllDetailedCustomersDto());
        return "allcustomers";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           Model model) {
        boolean success = customerService.register(firstName, lastName, email);
        if (!success) {
            model.addAttribute("error", "Registration failed. Email may already be in use.");
            return "register";
        }
        return "redirect:/allcustomers";
    }

    @GetMapping("/editcustomer/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        model.addAttribute("customer", customerService.getCustomerById(id));
        return "editcustomer";
    }

    @PostMapping("/editcustomer")
    public String updateCustomer(@RequestParam Long id,
                                 @RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email) {
        customerService.updateCustomer(id, firstName, lastName, email);
        return "redirect:/allcustomers";
    }


}
