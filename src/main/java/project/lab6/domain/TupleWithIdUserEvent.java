package project.lab6.domain;

import java.util.Objects;

public class TupleWithIdUserEvent {
    private final Long idUser;
    private final Long idEvent;

    public TupleWithIdUserEvent(Long idUser, Long idEvent) {
        this.idUser = idUser;
        this.idEvent = idEvent;
    }

    /**
     * @return the user id
     */
    public Long getIdUser() {
        return idUser;
    }

    /**
     * @return the event id
     */
    public Long getIdEvent() {
        return idEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TupleWithIdUserEvent that = (TupleWithIdUserEvent) o;
        return idUser.equals(that.idUser) && idEvent.equals(that.idEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idEvent);
    }
}
