package project.lab6.domain.entities.chat;

import javafx.scene.paint.Color;
import project.lab6.domain.entities.Entity;

public class Chat extends Entity<Long> {
    private final String name;
    private final boolean isPrivateChat;
    private Color color;

    /**
     * constructor with id
     *
     * @param id            Long
     * @param name          String
     * @param color         Color
     * @param isPrivateChat boolean
     */
    public Chat(Long id, String name, Color color, boolean isPrivateChat) {
        setId(id);
        this.name = name;
        this.color = color;
        this.isPrivateChat = isPrivateChat;

    }

    /**
     * constructor
     *
     * @param name          String
     * @param color         Color
     * @param isPrivateChat boolean
     */
    public Chat(String name, Color color, boolean isPrivateChat) {
        this(null, name, color, isPrivateChat);
    }

    /**
     * @return chat's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return chat's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * sets the color of the chat to newColor
     *
     * @param newColor
     */
    public void setColor(Color newColor) {
        color = newColor;
    }

    /**
     * @return true if the chat is private and false otherwise
     */
    public boolean isPrivateChat() {
        return isPrivateChat;
    }
}
