package adeo.leroymerlin.cdp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    EventRepository eventRepository = mock(EventRepository.class);
    EventService eventService = new EventService(eventRepository);

    @Test
    void testGetEvents() {
        //GIVEN events stored in databse (no event in this case)
        List<Event> expectedEvents = new ArrayList<>();
        when(eventRepository.findAllBy()).thenReturn(expectedEvents);

        //WHEN retrieving all events
        List<Event> returnedEventList = eventService.getEvents();

        //THEN All the events are returned
        assertEquals(expectedEvents, returnedEventList);
    }

    @Test
    void testDelete() {
        //GIVEN an event stored in database
        Long eventId = 1L;

        //WHEN trying to delete the event
        eventService.delete(eventId);

        //THEN the repository delete method is called
        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void testFindById() {
        //GIVEN an event stored in database
        Long eventId = 1L;
        Event expectedEvent = new Event();
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(expectedEvent));

        //WHEN trying to get the event by Id
        Optional<Event> event = eventService.findById(eventId);

        //THEN the event is returned
        assertEquals(expectedEvent, event.orElse(null));
    }

    @Test
    void testUpdate() {
        //GIVEN an event stored in database
        Event event = new Event();

        //WHEN trying to delete the event
        eventService.update(event);

        //THEN the repository save method is called
        verify(eventRepository).save(event);
    }

    @Test
    void testGetFilteredEvents() {
        //GIVEN events stored in database
        List<Event> eventList = Arrays.asList(
                createEvent(1L, "event_1", 3, Set.of(createBand("band_1", Set.of(createMember("member_1"), createMember("member_2"))))),
                createEvent(2L, "event_2", 4, Set.of(createBand("band_x", Set.of(createMember("member_xx"), createMember("member_yy")))))
        );
        when(eventService.getEvents()).thenReturn(eventList);

        //WHEN trying to get events filtered according to a pattern
        List<Event> filteredEvents = eventService.getFilteredEvents("_1");

        //THEN a list of filtered events is returned
        assertEquals(1, filteredEvents.size());
        assertEquals(1L, filteredEvents.get(0).getId());
        assertEquals("event_1 [1]", filteredEvents.get(0).getTitle());
        assertEquals("band_1 [2]", filteredEvents.get(0).getBands().iterator().next().getName());
        assertTrue(filteredEvents.get(0).getBands().iterator().next().getMembers().stream().anyMatch(member -> "member_1".equals(member.getName())));
    }

    private Event createEvent(Long id, String title, int nbStars, Set<Band> bands) {
        Event event = new Event();
        event.setId(id);
        event.setTitle(title);
        event.setNbStars(nbStars);
        event.setBands(bands);
        return event;
    }

    private Band createBand(String name, Set<Member> members) {
        Band band = new Band();
        band.setName(name);
        band.setMembers(members);
        return band;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        return member;
    }
}