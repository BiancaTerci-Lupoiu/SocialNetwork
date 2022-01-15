package project.lab6.domain.entities.events;

import project.lab6.domain.TupleWithIdUserEvent;
import project.lab6.domain.entities.Entity;

import java.time.LocalDateTime;

public class Subscription extends Entity<TupleWithIdUserEvent> {
    private final LocalDateTime date;

    /**
     * constructor
     * @param idUser
     * @param idEvent
     * @param date
     */
    public Subscription(Long idUser, Long idEvent, LocalDateTime date) {
        this.date = date;
        setId(new TupleWithIdUserEvent(idUser, idEvent));
    }

    /**
     * @return the date when the subscription was made
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @return the id of the user that made the subscription
     */
    public Long getIdUser() {
        return getId().getIdUser();
    }

    /**
     * @return the id of the event to which the subscription was made
     */
    public Long getIdEvent() {
        return getId().getIdEvent();
    }
}
