package project.lab6.domain.dtos;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import project.lab6.utils.Constants;
import project.lab6.utils.Images;
import project.lab6.utils.Lazy;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GroupChatDTO extends ChatDTO {

    private final String chatName;
    public GroupChatDTO(Long idChat, String chatName, Color color, Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users) {
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

    @Override
    public Image getImage(Long idLoggedUser) {
        return Images.getImage("chats", Constants.PATH_DEFAULT_GROUP_CHAT_IMAGE, getIdChat());
    }

    @Override
    public void saveImage(String path) throws IOException {
        Images.saveImage("chats",getIdChat(), path);
    }
}
