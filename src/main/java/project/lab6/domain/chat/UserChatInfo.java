package project.lab6.domain.chat;

import project.lab6.domain.Entity;
import project.lab6.domain.Tuple;

public class UserChatInfo extends Entity<Tuple<Long, Long>> {
    private final String nickname;

    public UserChatInfo(Long idChat, Long idUser, String nickname) {
        setId(new Tuple<>(idChat, idUser));
        this.nickname = nickname;
    }

    public Long getIdChat() {
        return getId().getLeft();
    }

    public Long getIdUser() {
        return getId().getRight();
    }

    public String getNickname() {
        return nickname;
    }
}
