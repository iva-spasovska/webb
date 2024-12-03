package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.EventBooking;

import java.util.List;

public interface EventBookingService {
    EventBooking placeBooking(String eventName, String attendeeName, String attendeeAddress, Long numberOfTickets);
    List<EventBooking> listAll();
    List<EventBooking> searchBookings(String searchText, Long numberOfTickets);
}