package org.example.assignment_backend_one.Services;

import org.example.assignment_backend_one.Models.AppUser;
import org.example.assignment_backend_one.Repositories.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

   private final AppUserRepository appUserRepository;

   public RegisterService(AppUserRepository appUserRepository){
       this.appUserRepository = appUserRepository;

   }
   public boolean register( String firstName, String lastName, String email){

       if( firstName == null && lastName == null && email == null ){
           return false;
       }
       if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()){
           return false;
       }
       if (appUserRepository.findById(id).isPresent()){
           return false;
       }
        AppUser appUser = new AppUser();
        appUser.setFirstname(firstName);
        appUser.setLastname(lastName);
        appUser.setEmail(email);
        appUserRepository.save(appUser);

        return true;

   }

}
