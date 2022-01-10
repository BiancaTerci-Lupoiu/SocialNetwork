package project.lab6.domain.dtos;

import javafx.scene.paint.Color;
import project.lab6.config.Config;
import project.lab6.utils.Constants;
import project.lab6.utils.Lazy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GroupChatDTO extends ChatDTO {

    private final String chatName;
    private final byte[] image;
    public GroupChatDTO(Long idChat, String chatName, Color color, Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users, byte[] image) {
        super(idChat, color, false, messages, users);
        this.chatName = chatName;
        if (image == null) {
            try (InputStream stream = getDefaultImage()) {
                image = stream.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
        this.image=image;
    }

    private InputStream getDefaultImage() throws FileNotFoundException {
        String path = Config.class.getClassLoader()
                .getResource(Constants.PATH_DEFAULT_GROUP_CHAT_IMAGE)
                .getPath().replace("%20", " ");
        return new FileInputStream(path);
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
    public byte[] getImage(Long idLoggedUser) {
        return new byte[0];
    }
}
