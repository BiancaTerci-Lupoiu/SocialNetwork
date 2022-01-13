package project.lab6.domain.dtos;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import project.lab6.utils.Lazy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class ChatDTO {
    private final Long idChat;
    private final Color color;
    private final boolean isPrivateChat;
    private final Lazy<List<MessageDTO>> messages;
    private final Lazy<List<UserChatInfoDTO>> users;

    protected ChatDTO(Long idChat, Color color, boolean isPrivateChat, Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users) {
        this.idChat = idChat;
        this.color = color;
        this.isPrivateChat = isPrivateChat;
        this.messages = messages;
        this.users = users;
    }

    /**
     * Creates a PrivateChatDTO or a GroupChatDTO and returns it
     *
     * @param name          The name of the chat. This argument will be ignored if isPrivateChat is true
     * @param color         The color of the chat
     * @param isPrivateChat True if the chat is private and false otherwise
     * @return ChatDTO
     */
    public static ChatDTO createChatDTO(Long idChat, String name, Color color, boolean isPrivateChat, Lazy<List<MessageDTO>> messages, Lazy<List<UserChatInfoDTO>> users) {
        if (isPrivateChat)
            return new PrivateChatDTO(idChat, color, messages, users);
        else
            return new GroupChatDTO(idChat, name, color, messages, users);
    }

    /**
     * @param idLoggedUser
     * @return the name of the chat, according to the logged user
     */
    public abstract String getName(Long idLoggedUser);

    /**
     * @return the idChat of the ChatDTO
     */
    public Long getIdChat() {
        return idChat;
    }

    /**
     * @return the color of the ChatDTO
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return true if the chat is private and false otherwise
     */
    public boolean isPrivateChat() {
        return isPrivateChat;
    }

    /**
     * @return the messages list of the ChatDTO
     */
    public List<MessageDTO> getMessages() {
        return messages.get();
    }

    /**
     * @return the last messageDTO from the chat, or null if there are no messages in chat
     */
    public MessageDTO getLastMessage(){
        List<MessageDTO> allMessagesDTO=getMessages();
        if(allMessagesDTO.isEmpty())
            return null;
        return allMessagesDTO.get(allMessagesDTO.size()-1);
    }

    /**
     * @return the users list of the ChatDTO
     */
    public List<UserChatInfoDTO> getUsersInfo() {
        return users.get();
    }

    /**
     * @return the image of the chat, according to the logged user
     */
    public abstract Image getImage(Long idLoggedUser);

    public abstract void saveImage(String path) throws IOException;
}
