package project.lab6.domain.dtos;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ChatDTO {
    private final String name;
    private final Color color;
    private final boolean isPrivateChat;
    private final List<MessageDTO> messages;
    private final List<UserChatInfoDTO> users;

    public ChatDTO(String name, Color color, boolean isPrivateChat) {
        this.name = name;
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

    public String getName() {
        return name;
    }

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
}
