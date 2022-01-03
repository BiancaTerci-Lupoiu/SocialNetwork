package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatDTO {
    private final Long idChat;
    private final Color color;
    private final boolean isPrivateChat;
    private final List<MessageDTO> messages;
    private final List<UserChatInfoDTO> users;

    /**
     * constructor
     * @param idChat
     * @param color
     * @param isPrivateChat
     */
    protected ChatDTO(Long idChat, Color color, boolean isPrivateChat) {
        this.idChat = idChat;
        this.color = color;
        this.isPrivateChat = isPrivateChat;
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }

    /**
     * adds a UserChatInfoDTO to the users list of the ChatDTO
     * @param userInfo
     */
    public void addUserInfo(UserChatInfoDTO userInfo)
    {
        users.add(userInfo);
    }

    /**
     * adds a MessageDTO to the messages list of the ChatDTO
     * @param message
     */
    public void addMessage(MessageDTO message)
    {
        messages.add(message);
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
        return messages;
    }

    /**
     * @return the users list of the ChatDTO
     */
    public List<UserChatInfoDTO> getUsersInfo() {
        return users;
    }

    /**
     * Creates a PrivateChatDTO or a GroupChatDTO and returns it
     * @param name The name of the chat. This argument will be ignored if isPrivateChat is true
     * @param color The color of the chat
     * @param isPrivateChat True if the chat is private and false otherwise
     * @return ChatDTO
     */
    public static ChatDTO createChatDTO(Long idChat,String name, Color color, boolean isPrivateChat)
    {
        if(isPrivateChat)
            return new PrivateChatDTO(idChat,color);
        else
            return new GroupChatDTO(idChat,name,color);
    }
}
