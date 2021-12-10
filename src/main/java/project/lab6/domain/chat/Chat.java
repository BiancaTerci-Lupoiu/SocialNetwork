package project.lab6.domain.chat;

import javafx.scene.paint.Color;
import project.lab6.domain.Entity;

public class Chat extends Entity<Long> {
    private final String name;
    private final Color color;
    private final boolean isPrivateChat;
    public Chat(Long id, String name, Color color, boolean isPrivateChat) {
        setId(id);
        this.name = name;
        this.color = color;
        this.isPrivateChat = isPrivateChat;
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
}
