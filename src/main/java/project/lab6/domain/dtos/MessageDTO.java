package project.lab6.domain.dtos;

import project.lab6.domain.chat.Chat;
import project.lab6.utils.Lazy;

import java.time.LocalDateTime;

public class MessageDTO {
    private final Long id;
    private final Chat chat;
    private final String text;
    private final LocalDateTime date;
    private final UserChatInfoDTO userFrom;
    private final Lazy<MessageDTO> repliedMessage;

    public MessageDTO(Long id, Chat chat, String text, LocalDateTime date, UserChatInfoDTO userFrom, Lazy<MessageDTO> repliedMessage) {
        this.id = id;
        this.chat = chat;
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
     * @return the chat in which this message was send
     */
    public Chat getChat() {
        return chat;
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
    public MessageDTO getRepliedMessage() {
        return repliedMessage.get();
    }
}
