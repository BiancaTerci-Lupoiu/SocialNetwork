package project.lab6.domain;

import java.util.Objects;

public class TupleWithIdUserEvent {
    private final Long idUser;
    private final Long idEvent;

    public TupleWithIdUserEvent(Long idUser, Long idEvent) {
        this.idUser = idUser;
        this.idEvent = idEvent;
    }

    public Long getIdUser() {
        return idUser;
    }

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
