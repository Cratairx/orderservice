package org.example.assignment_backend_one.DTO;


import jdk.jshell.Snippet;
import org.example.assignment_backend_one.Models.Room;

import java.time.LocalDate;

/*@Data


@AllArgsConstructor
@NoArgsConstructor
@Builder*/
public class CustomerDTO {
    public CustomerDTO() {

    }
    public CustomerDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public CustomerDTO(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public CustomerDTO(Long id, String firstName, String lastName, Room room, LocalDate startDate, LocalDate endDate) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
