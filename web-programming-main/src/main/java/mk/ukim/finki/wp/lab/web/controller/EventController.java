package mk.ukim.finki.wp.lab.web.controller;

import mk.ukim.finki.wp.lab.model.Event;
import mk.ukim.finki.wp.lab.model.Location;
import mk.ukim.finki.wp.lab.service.EventService;
import mk.ukim.finki.wp.lab.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;

    public EventController(EventService eventService, LocationService locationService) {
        this.eventService = eventService;
        this.locationService = locationService;
    }

    //@RequestMapping(method = RequestMethod.GET, value="/events")
    @GetMapping
    public String getEventsPage(@RequestParam(required = false) Long locationId,
                                @RequestParam(required = false) String error,
                                Model model) {
        if(error != null && !error.isEmpty()) {
            model.addAttribute("hasError", Optional.of(true));
            model.addAttribute("error", error);
        }

        List<Event> events;
        if (locationId != null) {
            events = this.eventService.findByLocationId(locationId);
        } else {
            events = this.eventService.listAll();
        }

        List<Location> locations = this.locationService.findAll();
        model.addAttribute("events", events);
        model.addAttribute("locations", locations);
        return "listEvents";
    }

    @PostMapping
    public String searchEvents(@RequestParam(required = false) String searchText,
                               @RequestParam(defaultValue = "0") Double minRating,
                               Model model) {
        List<Event> events = this.eventService.searchEvents(searchText, minRating);
        model.addAttribute("events", events);
        return "listEvents";
    }


    @PostMapping("/add")
    public String saveEvent(@RequestParam String name,
                            @RequestParam String description,
                            @RequestParam Double popularityScore,
                            @RequestParam Long locationId) {
        Location location = this.locationService.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        if(location != null) {
            this.eventService.addEvent(name, description, popularityScore, locationId);
        }
        return "redirect:/events";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam String description,
                            @RequestParam Double popularityScore,
                            @RequestParam Long locationId) {
        if(this.eventService.findById(id).isPresent()) {
            Event event = this.eventService.findById(id).get();
            event.setName(name);
            event.setDescription(description);
            event.setPopularityScore(popularityScore);
            event.setLocation(this.locationService.findById(locationId).orElse(null));
            this.eventService.editEvent(id, event.getName(), event.getDescription(), event.getPopularityScore(), event.getLocation().getId());
            return "redirect:/events";
        }
        return "redirect:/events?error=EventNotFound";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        this.eventService.deleteById(id);
        return "redirect:/events";
    }

    @GetMapping("/edit-form/{id}")
    public String getEditEventForm(@PathVariable Long id, Model model){
        if(this.eventService.findById(id).isPresent()) {
            Event event = this.eventService.findById(id).get();
            List<Location> locations = this.locationService.findAll();
            model.addAttribute("event", event);
            model.addAttribute("locations", locations);
            return "add-event";
        }
        return "redirect:/events?error=EventNotFound";
    }

    @GetMapping("/add-form")
    public String addEventPage(Model model) {
        List<Location> locations = this.locationService.findAll();
        model.addAttribute("locations", locations);
        return "add-event";
    }

    @PostMapping("/increment/{id}")
    public String increment(@PathVariable Long id) {
        if (this.eventService.findById(id).isPresent()) {
            this.eventService.incrementPopularityScore(id);
            return "redirect:/events";
        }
        return "redirect:/events?error=EventNotFound";
    }

    @PostMapping("/decrement/{id}")
    public String decrement(@PathVariable Long id) {
        if (this.eventService.findById(id).isPresent()) {
            this.eventService.decrementPopularityScore(id);
            return "redirect:/events";
        }
        return "redirect:/events?error=EventNotFound";
    }
}
