package mk.ukim.finki.wp.lab.repository.impl;

import mk.ukim.finki.wp.lab.model.Event;
import mk.ukim.finki.wp.lab.model.Location;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryEventRepository {
    private List<Event> events;
    private final InMemoryLocationRepository locationRepository;

    public InMemoryEventRepository(InMemoryLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        this.events = new ArrayList<>();
        for(int i = 0;i<10;i++){
            events.add(new Event("Event"+i,
                    "Description"+i,
                    (double) i+1,
                    locationRepository.findAll().get(i<5 ? i : i-5)));
        }
    }

    public List<Event> findAll() {
        return events;
    }

    public Optional<Event> findById(Long id) {
        return events.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    public List<Event> searchEvents(String text){
        return events.stream().filter(r -> r.getName().contains(text)
                || r.getDescription().contains(text)).collect(Collectors.toList());

    }

    public Optional<Event> save(String name, String description, Double popularityScore, Location location) {
        events.removeIf(event -> event.getName().equals(name));
        Event event = new Event(name, description, popularityScore, location);
        events.add(event);
        return Optional.of(event);
    }

    public void deleteById(Long id){
        events.removeIf(r -> r.getId().equals(id));
    }

    public void disableById(Long id, boolean isDisabled) {
        findById(id).ifPresent(event -> event.setDisabled(isDisabled));
    }
}
