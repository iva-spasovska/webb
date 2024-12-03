package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.model.EventBooking;
import mk.ukim.finki.wp.lab.service.EventBookingService;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.List;

@WebServlet(name="event-booking-servlet", urlPatterns = "/servlet/eventBooking")
public class EventBookingServlet extends HttpServlet {
    private final SpringTemplateEngine springTemplateEngine;
    private final EventBookingService eventBookingService;

    public EventBookingServlet(SpringTemplateEngine springTemplateEngine, EventBookingService eventBookingService) {
        this.springTemplateEngine = springTemplateEngine;
        this.eventBookingService = eventBookingService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        context.setVariable("bookings", eventBookingService.listAll());

        this.springTemplateEngine.process("bookingConfirmation.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String eventName = req.getParameter("eventName");
        String attendeeName = req.getServerName();
//        String attendeeName = req.getHeader("User-Agent");
//        String attendeeAddress = req.getLocalAddr();
        String attendeeAddress = req.getRemoteAddr();
        Long numberOfTickets = Long.valueOf(req.getParameter("numberOfTickets"));

        EventBooking booking = eventBookingService.placeBooking(eventName, attendeeName, attendeeAddress, numberOfTickets);
        List<EventBooking> bookings = eventBookingService.listAll();

        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);
        context.setVariable("eventName", booking.getEventName());
        context.setVariable("attendeeName", booking.getAttendeeName());
        context.setVariable("attendeeAddress", booking.getAttendeeAddress());
        context.setVariable("numberOfTickets", booking.getNumberOfTickets());

        context.setVariable("bookings",bookings);

        this.springTemplateEngine.process("bookingConfirmation.html", context, resp.getWriter());
    }
}
