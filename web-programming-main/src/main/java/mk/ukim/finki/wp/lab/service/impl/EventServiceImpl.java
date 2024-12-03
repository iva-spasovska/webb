package mk.ukim.finki.wp.lab.service.impl;

import jakarta.transaction.Transactional;
import mk.ukim.finki.wp.lab.model.Event;
import mk.ukim.finki.wp.lab.model.Location;
import mk.ukim.finki.wp.lab.model.exceptions.LocationNotFoundException;
import mk.ukim.finki.wp.lab.repository.jpa.EventRepository;
import mk.ukim.finki.wp.lab.repository.jpa.LocationRepository;
import mk.ukim.finki.wp.lab.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;

    public EventServiceImpl(EventRepository eventRepository, LocationRepository locationRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Event> listAll() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }


    @Override
    public List<Event> searchEvents(String text, Double minRating) {
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(text).stream()
                .filter(e -> e.getPopularityScore() >= minRating)
                .collect(Collectors.toList());
        return events;
    }

    @Override
    public Optional<Event> addEvent(String name, String description, double popularityScore, Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        Event newEvent = new Event(name, description, popularityScore, location);
        return Optional.of(eventRepository.save(newEvent));
    }

    @Override
    public Optional<Event> editEvent(Long eventId, String name, String description, double popularityScore, Long locationId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event != null) {
            Location location = this.locationRepository.findById(locationId)
                    .orElseThrow(() -> new LocationNotFoundException(locationId));
            event.setName(name);
            event.setDescription(description);
            event.setPopularityScore(popularityScore);
            event.setLocation(location);
        }
        return Optional.of(eventRepository.save(event));
    }

    @Override
    public void deleteById(Long id) {
        this.eventRepository.deleteById(id);
    }

    @Override
    public List<Event> findAllByLocation_Id(Long locationId) {
        return this.eventRepository.findAllByLocation_Id(locationId);
    }

    @Override
    public void incrementPopularityScore(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if(event != null) {
            event.setPopularityScore(event.getPopularityScore() + 1.0);
            event.setDisabled(true);
            eventRepository.save(event);
        }
    }

    @Override
    public void decrementPopularityScore(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if(event != null) {
            event.setPopularityScore(event.getPopularityScore() - 1.0);
            event.setDisabled(true);
            eventRepository.save(event);
        }
    }

    @Override
    public void disableButtons(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if(event != null) {
            event.setDisabled(true);
            eventRepository.save(event);
        }
    }

    @Override
    public List<Event> findByLocationId(Long locationId) {
        return this.eventRepository.findAllByLocation_Id(locationId);
    }
}