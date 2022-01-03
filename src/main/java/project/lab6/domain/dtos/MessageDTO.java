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

    /**
     * @return the id of the MessageDTO
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the text of the MessageDTO
     */
    public String getText() {
        return text;
    }

    /**
     * @return the date of the MessageDTO
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @return the userFrom(UserChatInfoDTO) of the MessageDTO
     */
    public UserChatInfoDTO getUserFromInfo() {
        return userFrom;
    }

    /**
     * @return the repliedMessage of the MessageDTO
     */
    public Message getRepliedMessage() {
        return repliedMessage;
    }
}
