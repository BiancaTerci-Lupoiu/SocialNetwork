package project.lab6.domain.validators;

import project.lab6.domain.entities.events.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event entity) throws ValidationException {

        String errors = "";
        if (entity.getTitle().trim().isEmpty() || entity.getTitle() == null)
            errors += "invalid title!\n";
        if (entity.getIdUserOwner() == null)
            errors += "invalid owner id!\n";
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
