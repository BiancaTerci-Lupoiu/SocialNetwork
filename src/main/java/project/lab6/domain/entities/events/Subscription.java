package project.lab6.domain.entities.events;

import project.lab6.domain.TupleWithIdUserEvent;
import project.lab6.domain.entities.Entity;

import java.time.LocalDateTime;

public class Subscription extends Entity<TupleWithIdUserEvent> {
    private final LocalDateTime date;

    public Subscription(Long idUser, Long idEvent, LocalDateTime date) {
        this.date = date;
        setId(new TupleWithIdUserEvent(idUser, idEvent));
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getIdUser() {
        return getId().getIdUser();
    }

    public Long getIdEvent() {
        return getId().getIdEvent();
    }
}
