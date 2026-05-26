

import org.example.assignment_backend_one.DTO.DetailedCustomerDTO;
import org.example.assignment_backend_one.Models.Booking;
import org.example.assignment_backend_one.Models.Customer;
import org.example.assignment_backend_one.Repositories.CustomerRepository;
import org.example.assignment_backend_one.Services.BookingService;
import org.example.assignment_backend_one.Services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Test
    void registerShouldReturnFalseWhenEmailAlreadyInUse() {
        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(new Customer()));

        boolean result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertFalse(result);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void registerShouldReturnTrueWhenEmailNotInUse() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        boolean result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertTrue(result);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }

    @Test
    void deleteCustomerShouldReturnFalseWhenHasBooking() {
        Customer customer = new Customer();
        customer.setBookings(List.of(new Booking()));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        boolean result = customerServiceImpl.deleteCustomer(1L);

        assertFalse(result);
        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void updateCustomerShouldReturnTrue() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerServiceImpl.updateCustomer(1L, "Emil", "D", "emil@example.com");

        assertEquals("Emil", customer.getFirstName());
        assertEquals("D", customer.getLastName());
        assertEquals("emil@example.com", customer.getEmail());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals(customer, customerServiceImpl.getCustomerById(1L));

    }

    @Test
    void getAllDetailedCustomersDTOShouldReturnCustomers() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setBookings(List.of(new Booking()));

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<DetailedCustomerDTO> result = customerServiceImpl.getAllDetailedCustomersDto();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());

    }


}
