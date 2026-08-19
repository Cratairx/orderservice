package org.example.assignment_backend_one.DTO;
import org.example.assignment_backend_one.Models.Room;
import java.time.LocalDate;

/*@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder*/
public class BookingDTO {

    private Long id;
    private CustomerDTO customer;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;

    public BookingDTO() {

    }
    public BookingDTO(Long id, CustomerDTO customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
