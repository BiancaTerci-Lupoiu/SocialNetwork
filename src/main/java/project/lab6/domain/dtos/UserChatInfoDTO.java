package project.lab6.domain.dtos;

import project.lab6.domain.User;

public class UserChatInfoDTO {
    private final User user;
    private final String nickname;
    private final Long idChat;

    public UserChatInfoDTO(Long idChat, User user, String nickname) {
        this.idChat = idChat;
        this.user = user;
        this.nickname = nickname;
    }

    public User getUser() {
        return user;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getIdChat() {
        return idChat;
    }
}
