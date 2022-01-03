package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

public class GroupChatDTO extends ChatDTO {

    private final String chatName;

    public GroupChatDTO(Long idChat, String chatName, Color color) {
        super(idChat, color, false);
        this.chatName = chatName;
    }

    /**
     * @param idLoggedUser
     * @return the name of the chat, according to the logged user
     */
    @Override
    public String getName(Long idLoggedUser) {
        return chatName;
    }
}
