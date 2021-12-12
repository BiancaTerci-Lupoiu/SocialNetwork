package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

public class GroupChatDTO extends ChatDTO {

    private final String chatName;

    public GroupChatDTO(String chatName, Color color) {
        super(color, false);
        this.chatName = chatName;
    }

    @Override
    public String getName(Long idLoggedUser) {
        return chatName;
    }
}
