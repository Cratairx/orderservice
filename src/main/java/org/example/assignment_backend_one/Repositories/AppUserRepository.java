package org.example.assignment_backend_one.Repositories;

import org.example.assignment_backend_one.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    Optional<Object> findByEmail(String email);

    // här kan vi lägga till egna om vi vill

}
