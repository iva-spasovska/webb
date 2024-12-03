package mk.ukim.finki.wp.lab.service;

import mk.ukim.finki.wp.lab.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> listAll();
    Optional<Event> findById(Long id);
    List<Event> searchEvents(String text, Double minRating);
    Optional<Event> addEvent(String name, String description, double popularityScore, Long locationId);
    Optional<Event> editEvent(Long id, String name, String description, double popularityScore, Long locationId);
    void deleteById(Long id);
    List<Event> findAllByLocation_Id(Long locationId);
    void incrementPopularityScore(Long id);
    void decrementPopularityScore(Long id);
    void disableButtons(Long id);
    List<Event> findByLocationId(Long locationId);
}