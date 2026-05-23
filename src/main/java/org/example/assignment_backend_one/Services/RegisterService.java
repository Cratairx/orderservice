package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.AppUser;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.AppUserRepository;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.springframework.stereotype.Service;

/*@Service
public class RegisterService {

   private final CustomerRepository customerRepository;

   public RegisterService(CustomerRepository customerRepository){
       this.customerRepository = customerRepository;

   }
   public boolean register( String firstName, String lastName, String email){

       if( firstName == null && lastName == null && email == null ){
           return false;
       }
       if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
           return false;
       }

       if (customerRepository.findByEmail(email).isPresent()){
           return false;
       }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customerRepository.save(customer);

        return true;
   }

}*/
