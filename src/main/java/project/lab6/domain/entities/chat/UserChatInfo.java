package project.lab6.domain.entities.chat;

import project.lab6.domain.TupleWithIdChatUser;
import project.lab6.domain.entities.Entity;

public class UserChatInfo extends Entity<TupleWithIdChatUser> {
    private final String nickname;

    /**
     * constructor
     *
     * @param idChat
     * @param idUser
     * @param nickname
     */
    public UserChatInfo(Long idChat, Long idUser, String nickname) {
        setId(new TupleWithIdChatUser(idChat, idUser));
        this.nickname = nickname;
    }

    /**
     * @return the idChat of the UserChatInfo
     */
    public Long getIdChat() {
        return getId().getIdChat();
    }

    /**
     * @return the idUser of the UserChatInfo
     */
    public Long getIdUser() {
        return getId().getIdUser();
    }

    /**
     * @return the nickname of the UserChatInfo
     */
    public String getNickname() {
        return nickname;
    }
}
