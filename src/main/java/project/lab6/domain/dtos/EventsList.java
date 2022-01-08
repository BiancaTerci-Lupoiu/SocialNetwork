package project.lab6.domain.dtos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class EventsList extends ArrayList<EventForUserDTO> {
    private final Long idUser;

    public EventsList(Long idUser, Collection<EventForUserDTO> collection) {
        super(collection);
        this.idUser = idUser;
    }

    public EventsList getWithSubscription(boolean isSubscribed) {
        return new EventsList(idUser, stream().filter(getWithSubscriptionPredicate(isSubscribed)).toList());
    }

    public EventsList getOwnEvents() {
        return new EventsList(idUser, stream()
                .filter(getOwnEventsPredicate())
                .toList());
    }

    public Predicate<EventForUserDTO> getOwnEventsPredicate() {
        return eventForUserDTO -> eventForUserDTO.getOwner().getId().equals(idUser);
    }

    public Predicate<EventForUserDTO> getWithSubscriptionPredicate(boolean isSubscribed) {
        return event -> event.isSubscribed() == isSubscribed;
    }
}
