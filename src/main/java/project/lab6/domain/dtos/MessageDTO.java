package project.lab6.domain.dtos;

import project.lab6.domain.chat.Message;

import java.time.LocalDateTime;

public class MessageDTO {
    private final Long id;
    private final String text;
    private final LocalDateTime date;
    private final UserChatInfoDTO userFrom;
    private final Message repliedMessage;

    public MessageDTO(Long id, String text, LocalDateTime date, UserChatInfoDTO userFrom, Message repliedMessage) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.userFrom = userFrom;
        this.repliedMessage = repliedMessage;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UserChatInfoDTO getUserFrom() {
        return userFrom;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }
}
