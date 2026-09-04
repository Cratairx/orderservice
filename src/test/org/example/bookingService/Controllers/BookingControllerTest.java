package org.example.bookingService.Controllers;

import org.example.bookingService.Models.Booking;
import org.example.bookingService.Models.Room;
import org.example.bookingService.Services.BookingService;
import org.example.bookingService.Services.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @MockitoBean
    private RoomService roomService;

    @Test
    void createBooking_returns201_whenSuccessful() throws Exception {
        when(bookingService.createBooking(1L, 2L, java.time.LocalDate.parse("2026-09-10"), java.time.LocalDate.parse("2026-09-12")))
                .thenReturn(true);

        mockMvc.perform(post("/api/bookings")
                        .param("customerId", "1")
                        .param("roomId", "2")
                        .param("startDate", "2026-09-10")
                        .param("endDate", "2026-09-12"))
                .andExpect(status().isCreated());
    }

    @Test
    void createBooking_returns409_whenServiceRejects() throws Exception {
        when(bookingService.createBooking(anyLong(), anyLong(), any(), any())).thenReturn(false);

        mockMvc.perform(post("/api/bookings")
                        .param("customerId", "1")
                        .param("roomId", "2")
                        .param("startDate", "2026-09-10")
                        .param("endDate", "2026-09-12"))
                .andExpect(status().isConflict());
    }

    @Test
    void availableRooms_returns400_whenDatesMissing() throws Exception {
        mockMvc.perform(get("/api/bookings/available"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(roomService);
    }

    @Test
    void availableRooms_returns400_whenStartNotBeforeEnd() throws Exception {
        mockMvc.perform(get("/api/bookings/available")
                        .param("startDate", "2026-09-12")
                        .param("endDate", "2026-09-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void availableRooms_returnsOk_withRoomsList() throws Exception {
        when(roomService.getAvailableRooms(any(), any())).thenReturn(List.of(new Room(1L, "101", null)));

        mockMvc.perform(get("/api/bookings/available")
                        .param("startDate", "2026-09-10")
                        .param("endDate", "2026-09-12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].roomNumber").value("101"));
    }

    @Test
    void updateBooking_returnsOk_whenSuccessful() throws Exception {
        when(bookingService.updateBooking(eq(5L), anyLong(), any(), any())).thenReturn(true);

        mockMvc.perform(put("/api/bookings/5")
                        .param("roomId", "2")
                        .param("startDate", "2026-09-10")
                        .param("endDate", "2026-09-12"))
                .andExpect(status().isOk());
    }

    @Test
    void updateBooking_returns409_whenConflict() throws Exception {
        when(bookingService.updateBooking(eq(5L), anyLong(), any(), any())).thenReturn(false);

        mockMvc.perform(put("/api/bookings/5")
                        .param("roomId", "2")
                        .param("startDate", "2026-09-10")
                        .param("endDate", "2026-09-12"))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteBooking_returns204_whenDeleted() throws Exception {
        when(bookingService.deleteBooking(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/bookings/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBooking_returns404_whenNotFound() throws Exception {
        when(bookingService.deleteBooking(5L)).thenReturn(false);

        mockMvc.perform(delete("/api/bookings/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listBookings_returnsOk() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(new Booking()));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk());
    }

    @Test
    void existsBookings_returnsBooleanBody() throws Exception {
        when(bookingService.hasBookingsForCustomer(1L)).thenReturn(true);

        mockMvc.perform(get("/api/bookings/exists").param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}