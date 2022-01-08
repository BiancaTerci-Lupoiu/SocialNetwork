package project.lab6.domain.validators;

import project.lab6.domain.entities.events.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event entity) throws ValidationException {
        //TODO: Trebuie validat sa nu fie titlu si idOwner vide
    }
}
