package project.lab6.domain.dtos;

import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;

public class UserChatInfoDTO {
    private final User user;
    private final String nickname;

    public UserChatInfoDTO(User user, String nickname) {
        this.user = user;
        this.nickname = nickname;
    }

    public User getUser() {
        return user;
    }

    public String getNickname() {
        return nickname;
    }
}
