package org.example.assignment_backend_one.Config;


import org.example.assignment_backend_one.ENUMS.RoomType;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Models.Room;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Repositories.RoomRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public class Config implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final RoomRepository roomRepository;

    public Config(CustomerRepository customerRepository, RoomRepository roomRepository) {
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void run(String... args) {
        if (customerRepository.count() == 0) {
            customerRepository.saveAll(List.of(
                    new Customer(null, "John", "Doe", "john@example.com", List.of()),
                    new Customer(null, "Jane", "Smith", "jane@example.com", List.of()),
                    new Customer(null, "Bob", "Johnson", "bob@example.com", List.of())
            ));
        }

        if (roomRepository.count() == 0) {
            roomRepository.saveAll(List.of(
                    new Room(null, "101", RoomType.SINGLE),
                    new Room(null, "102", RoomType.DOUBLE),
                    new Room(null, "103", RoomType.SINGLE)
            ));
        }
    }
}