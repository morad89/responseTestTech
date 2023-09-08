package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A Service to manage events.
 */
@Service
public class EventService {

    /** The event repository */
    private final EventRepository eventRepository;

    /**
     * Constructor with params
     * @param eventRepository The event repository
     */
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * gets all events stored in db.
     * @return list of all events
     */
    public List<Event> getEvents() {
        return eventRepository.findAllBy();
    }

    /**
     * deletes an event by its Id.
     * @param id event's id
     */
    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    /**
     * gets an event by its Id.
     * @param id event's id
     * @return
     */
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * updates an event.
     * @param event the event to update
     */
    public void update(Event event) {
        eventRepository.save(event);
    }

    /**
     * gets a list of filtered events using a pattern.
     * @param query pattern to use as filter
     * @return a filtered event list
     */
    public List<Event> getFilteredEvents(String query) {
        List<Event> events = eventRepository.findAllBy();
        // Filter the events list in pure JAVA here
        return events.stream()
                .filter(event -> event.getBands().stream().
                        anyMatch(band -> band.getMembers().stream().
                                anyMatch(member -> member.getName()!= null && member.getName().contains(query))))
                .peek(this::addEventCounter)
                .collect(Collectors.toList());
    }

    private void addEventCounter(Event event) {
        event.setTitle(event.getTitle() + " [" + event.getBands().size() + "]");
        event.getBands().forEach(this::addBandCounter);
    }

    private void addBandCounter(Band band) {
        if(band.getName() != null && !band.getName().contains(" [" + band.getMembers().size() + "]")) {
            band.setName(band.getName() + " [" + band.getMembers().size() + "]");
        }
    }
}
