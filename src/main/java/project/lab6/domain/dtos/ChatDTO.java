package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatDTO {
    private final Color color;
    private final boolean isPrivateChat;
    private final List<MessageDTO> messages;
    private final List<UserChatInfoDTO> users;

    protected ChatDTO(Color color, boolean isPrivateChat) {
        this.color = color;
        this.isPrivateChat = isPrivateChat;
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void addUserInfo(UserChatInfoDTO userInfo)
    {
        users.add(userInfo);
    }

    public void addMessage(MessageDTO message)
    {
        messages.add(message);
    }

    public abstract String getName(Long idLoggedUser);

    public Color getColor() {
        return color;
    }

    public boolean isPrivateChat() {
        return isPrivateChat;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

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
    public static ChatDTO createChatDTO(String name, Color color, boolean isPrivateChat)
    {
        if(isPrivateChat)
            return new PrivateChatDTO(color);
        else
            return new GroupChatDTO(name,color);
    }
}
