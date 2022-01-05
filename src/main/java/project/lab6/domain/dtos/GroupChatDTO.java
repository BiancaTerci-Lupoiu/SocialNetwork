package project.lab6.domain.dtos;

import javafx.scene.paint.Color;
import project.lab6.utils.Lazy;

import java.util.List;

public class GroupChatDTO extends ChatDTO {

    private final String chatName;

    public GroupChatDTO(Long idChat, String chatName, Color color,  Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users) {
        super(idChat, color, false, messages, users);
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
