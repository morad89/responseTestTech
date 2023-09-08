package adeo.leroymerlin.cdp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Rest controller for managing events.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    /** The event service. */
    private final EventService eventService;

    /**
     * Constructor with params
     * @param eventService the Event service
     */
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Method to get list of all events
     * @return a list of all events stored in db.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Event> findEvents() {
        return eventService.getEvents();
    }

    /**
     * Method to get list of events filtered
     * @param query the pattern to use to filter the event's list
     * @return a filtered event list
     */
    @RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
    public List<Event> findEvents(@PathVariable String query) {
        return eventService.getFilteredEvents(query);
    }

    /**
     * Method to delete an event by its Id
     * @param id the Id of the event.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
    }

    /**
     * Method to update review of an event
     * @param id the Id of the event.
     * @param event the event instance populated with request data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateEvent(@PathVariable Long id, @RequestBody Event event) {
        Optional<Event> optionalEvent = eventService.findById(id);
        if(optionalEvent.isPresent()){
            Event eventToUpdate = optionalEvent.get();
            eventToUpdate.setNbStars(event.getNbStars());
            eventService.update(eventToUpdate);
        }
    }
}
