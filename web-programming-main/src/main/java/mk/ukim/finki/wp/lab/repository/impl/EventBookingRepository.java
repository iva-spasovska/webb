package mk.ukim.finki.wp.lab.repository.impl;

import mk.ukim.finki.wp.lab.model.EventBooking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventBookingRepository {
    private List<EventBooking> bookings;

    public EventBookingRepository() {
        this.bookings = new ArrayList<>();
    }

    public List<EventBooking> findAllBookings() {
        return bookings;
    }

    public List<EventBooking> searchBookings(String searchText, Long numberOfTickets) {
        return bookings.stream()
                .filter(booking ->
                        (searchText != null && booking.getEventName() != null && booking.getEventName().contains(searchText))
                                && (numberOfTickets != null && booking.getNumberOfTickets().equals(numberOfTickets))).collect(Collectors.toList());

    }

    public void addBooking(EventBooking booking) {
        bookings.add(booking);
    }
}
