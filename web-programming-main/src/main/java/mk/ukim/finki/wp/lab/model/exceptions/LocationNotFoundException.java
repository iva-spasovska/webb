package mk.ukim.finki.wp.lab.model.exceptions;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id) {
        super(String.format("Location with id '%d' was not found.", id));
    }
}
