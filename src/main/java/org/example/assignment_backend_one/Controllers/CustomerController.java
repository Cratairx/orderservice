package org.example.assignment_backend_one.Controllers;

import jakarta.servlet.http.HttpSession;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController( CustomerService customerService) {
                this.customerService = customerService;
    }

    @GetMapping("/editcustomer")
    public String editCustomer(@RequestParam Long id, Model model, HttpSession session) {
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        String email = (String) session.getAttribute("email");
        model.addAttribute("customer", customerService.getCustomerById(id));
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);

        return "editcustomer";
    }
}
