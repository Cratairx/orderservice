package org.example.assignment_backend_one.Repositories;

import org.example.assignment_backend_one.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {

}
