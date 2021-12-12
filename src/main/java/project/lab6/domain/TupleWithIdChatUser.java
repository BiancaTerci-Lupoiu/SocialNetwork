package project.lab6.domain;

import java.util.Objects;

public class TupleWithIdChatUser {
    private final Long idChat;
    private final Long idUser;

    public TupleWithIdChatUser(Long idChat, Long idUser) {
        this.idChat = idChat;
        this.idUser = idUser;
    }

    public Long getIdChat() {
        return idChat;
    }

    public Long getIdUser() {
        return idUser;
    }

    @Override
    public String toString() {
        return "TupleWithIdChatUser{" +
                "idChat=" + idChat +
                ", idUser=" + idUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TupleWithIdChatUser that = (TupleWithIdChatUser) o;
        return Objects.equals(idChat, that.idChat) && Objects.equals(idUser, that.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChat, idUser);
    }
}
