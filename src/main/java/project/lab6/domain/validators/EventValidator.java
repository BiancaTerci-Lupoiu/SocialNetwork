package project.lab6.domain.validators;

import project.lab6.domain.entities.events.Event;

import java.time.LocalDateTime;

public class EventValidator implements Validator<Event> {
    /**
     * verify if an Event is valid
     * @param entity - the entity to be validated
     * @throws ValidationException if the event's title is empty or null or,
     *                                 the event's idUserOwner is null or,
     *                                 the event's date is null or the date is before current date
     */
    @Override
    public void validate(Event entity) throws ValidationException {

        String errors = "";
        if (entity.getTitle().trim().isEmpty() || entity.getTitle() == null)
            errors += "invalid title!\n";
        if (entity.getIdUserOwner() == null)
            errors += "invalid owner id!\n";
        if (entity.getDate() == null || entity.getDate().isBefore(LocalDateTime.now()))
            errors += "invalid date! (it should be a future date)\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
