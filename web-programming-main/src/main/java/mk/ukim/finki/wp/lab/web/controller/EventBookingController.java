package mk.ukim.finki.wp.lab.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.ukim.finki.wp.lab.model.EventBooking;
import mk.ukim.finki.wp.lab.service.EventBookingService;
import mk.ukim.finki.wp.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/eventBooking")
public class EventBookingController {
    private final EventBookingService eventBookingService;

    public EventBookingController(EventBookingService eventBookingService, LocationService locationService) {
        this.eventBookingService = eventBookingService;
    }

    @GetMapping
    public String getEventBookingPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        List<EventBooking> bookings = this.eventBookingService.listAll();
        model.addAttribute("bookings", bookings);
        return "bookingConfirmation";
    }

    @PostMapping
    public String placeBooking(
            @RequestParam String eventName,
                               @RequestParam Long numberOfTickets,
                               HttpServletRequest request,
//                               @RequestParam String attendeeName,
//                               @RequestParam String attendeeAddress,
                               Model model) {
//        String eventName = request.getParameter("eventName");
        String attendeeName = request.getServerName();
        String attendeeAddress = request.getRemoteAddr();
//        Long numberOfTickets = Long.parseLong(request.getParameter("numberOfTickets"));

        EventBooking booking = eventBookingService.placeBooking(eventName, attendeeName, attendeeAddress, numberOfTickets);
        List<EventBooking> bookings = eventBookingService.listAll();

        model.addAttribute("eventName", booking.getEventName());
        model.addAttribute("attendeeName", booking.getAttendeeName());
        model.addAttribute("attendeeAddress", booking.getAttendeeAddress());
        model.addAttribute("numberOfTickets", booking.getNumberOfTickets());
        model.addAttribute("bookings", bookings);
        return "bookingConfirmation";

    }
}
