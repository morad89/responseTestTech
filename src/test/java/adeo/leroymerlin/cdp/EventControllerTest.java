package adeo.leroymerlin.cdp;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventControllerTest {

    EventService eventService = mock(EventService.class);
    EventController eventController = new EventController(eventService);

    @Test
    void testFindEvents() {
        //GIVEN events stored in databse
        List<Event> eventList = Arrays.asList(
                createEvent(1L, "event_1", 3),
                createEvent(2L, "event_2", 4)
        );
        when(eventService.getEvents()).thenReturn(eventList);

        //WHEN requesting to display all events
        List<Event> returnedEventList = eventController.findEvents();

        //THEN All the events are returned
        assertEquals(eventList, returnedEventList);
    }

    @Test
    void testDeleteEvent() {
        //GIVEN an event stored in database
        Long eventId = 1L;

        //WHEN requesting to delete the event
        eventController.deleteEvent(eventId);

        //THEN service delete method is called
        verify(eventService, times(1)).delete(eventId);
    }

    @Test
    void testUpdateEventReview() {
        //GIVEN an event stored in database
        Long eventId = 1L;
        Event updatedEvent = createEvent(1L, "event", 3);

        //WHEN retrieving the event from database
        when(eventService.findById(eventId)).thenReturn(Optional.of(createEvent(1L, "event", 0)));

        //WHEN updating the number of stars of the event
        eventController.updateEvent(eventId, updatedEvent);

        //THEN the changes are reflected and service update method is called
        ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(eventService, times(1)).update(argumentCaptor.capture());

        Event capturedEvent = argumentCaptor.getValue();
        assertEquals(updatedEvent.getNbStars(), capturedEvent.getNbStars());
    }

    private Event createEvent(Long id, String title, int nbStars) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setNbStars(nbStars);
        return event;
    }
}