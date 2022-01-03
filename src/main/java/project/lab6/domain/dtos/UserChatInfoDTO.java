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

    /**
     * @return the user of the UserChatInfoDTO
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the nickname of the UserChatInfoDTO
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return the idChat of the UserChatInfoDTO
     */
    public Long getIdChat() {
        return idChat;
    }
}
