package project.lab6.domain.chat;

import project.lab6.domain.Entity;
import project.lab6.domain.Tuple;
import project.lab6.domain.TupleWithIdChatUser;

public class UserChatInfo extends Entity<TupleWithIdChatUser> {
    private final String nickname;

    public UserChatInfo(Long idChat, Long idUser, String nickname) {
        setId(new TupleWithIdChatUser(idChat, idUser));
        this.nickname = nickname;
    }

    public Long getIdChat() {
        return getId().getIdChat();
    }

    public Long getIdUser() {
        return getId().getIdUser();
    }

    public String getNickname() {
        return nickname;
    }
}
