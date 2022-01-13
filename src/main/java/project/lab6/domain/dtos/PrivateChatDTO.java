package project.lab6.domain.dtos;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceException;
import project.lab6.utils.Lazy;

import java.io.IOException;
import java.util.List;

public class PrivateChatDTO extends ChatDTO {
    protected PrivateChatDTO(Long idChat, Color color, Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users) {
        super(idChat, color, true, messages, users);
    }

    private UserChatInfoDTO getOtherUser(Long idLoggedUser) {
        List<UserChatInfoDTO> info = getUsersInfo();
        int yoursIndex;
        for (yoursIndex = 0; yoursIndex < 2; yoursIndex++)
            if (info.get(yoursIndex).getUser().getId().equals(idLoggedUser))
                break;
        return info.get(yoursIndex ^ 1);
    }

    /**
     * @param idLoggedUser
     * @return the nickname of the other user (not the logged one) from the PrivateChat
     */
    @Override
    public String getName(Long idLoggedUser) {
        return getOtherUser(idLoggedUser).getNickname();
    }

    @Override
    public Image getImage(Long idLoggedUser) {
        return getOtherUser(idLoggedUser).getUser().getImage();
    }

    @Override
    public void saveImage(String path) {
        throw new ServiceException("Can't save a image in a private chat!");
    }
}
