package adeo.leroymerlin.cdp;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * a repository for managing Event entities in the database.
 */
@Transactional
public interface EventRepository extends Repository<Event, Long> {

    /**
     * saves an event entity in the database.
     * @param event The event to be saved.
     */
    void save(Event event);

    /**
     * deletes an event by its Id.
     * @param eventId The Id of the event to be deleted.
     */
    void deleteById(Long eventId);

    /**
     * finds an event by its Id.
     * @param id The Id of the event to find.
     * @return An optional containing the event if found, or empty if not found.
     */
    Optional<Event> findById(Long id);

    /**
     * retrieves a list of all events.
     * @return A list of all events in the database.
     */
    List<Event> findAllBy();
}
