package mk.ukim.finki.wp.lab.repository.impl;

import mk.ukim.finki.wp.lab.model.Location;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryLocationRepository {
    private List<Location> locations;

    public InMemoryLocationRepository() {
        this.locations = new ArrayList<Location>();
        for(int i = 0;i<5;i++){
            locations.add(new Location("LocationName"+i,"Address"+i, "Capacity: "+i, "LocationDescription"+i));
        }
    }

    public List<Location> findAll(){
        return locations;
    }

    public Optional<Location> findById(Long locationId) {
        return locations.stream().filter(i -> i.getId().equals(locationId)).findFirst();
    }
}
