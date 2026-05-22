package org.example.assignment_backend_one.Controllers;


import jakarta.servlet.http.HttpSession;
import org.example.assignment_backend_one.DTO.CustomerDTO;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Services.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController( CustomerService customerService) {
                this.customerService = customerService;
    }

    @GetMapping("/editcustomer/{id}")
    public String editCustomer(@PathVariable Long id, Model model, HttpSession session) {
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        String email = (String) session.getAttribute("email");

        model.addAttribute("customer", customerService.getCustomerById(id));
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
        return "editcustomer";
    }

   /* @PostMapping("/editcustomer")
    public String updateCustomer(@RequestParam Long id,
                                 @RequestParam String firstname,
                                 @RequestParam String lastname,
                                 @RequestParam String email) {
        customerService.updateCustomer(id, firstname, lastname, email);

        return "redirect:/index";
    }*/
    @PostMapping("/editcustomer")
    public String updateCustomer(@ModelAttribute CustomerDTO customer) {

       customerService.updateCustomer(customer.getId(), customer.getFirstName(), customer.getLastName(), customer.getEmail());
        return "redirect:/index";
    }


    ///deletecustomer/1
    @RequestMapping("/deletecustomer/{id}")
    public String deleteCustomer(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        boolean result = customerService.deleteCustomer(id);
        if (result) {
            model.addAttribute("customer", customerService.getCustomerById(id));
            return "redirect:/allcustomers";
        }
        model.addAttribute("error","Failed to delete customer");
        model.addAttribute("customers", customerService.getAllCustomers());
        return "/allcustomers";
    }

    @GetMapping("/customer")
    public String customer() {
        return "customer";
    }

    @GetMapping("/allcustomers")
    public String allcustomers(Model model) {
        model.addAttribute("customers", customerService.getAllCustomers());
        return "allcustomers";
    }

}
