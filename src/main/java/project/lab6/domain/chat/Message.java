package project.lab6.domain.chat;

import project.lab6.domain.Entity;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private final String text;
    private final LocalDateTime date;
    private final Long idUserFrom;
    private final Long idChat;
    private final Long idReplyMessage;

    /**
     * constructor
     *
     * @param text
     * @param date
     * @param idUserFrom
     * @param idChat
     * @param idReplyMessage
     */
    public Message(String text, LocalDateTime date, Long idUserFrom, Long idChat, Long idReplyMessage) {
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idChat = idChat;
        this.idReplyMessage = idReplyMessage;
    }

    /**
     * constructor with id
     *
     * @param id
     * @param text
     * @param date
     * @param idUserFrom
     * @param idChat
     * @param idReplyMessage
     */
    public Message(Long id, String text, LocalDateTime date, Long idUserFrom, Long idChat, Long idReplyMessage) {
        setId(id);
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idChat = idChat;
        this.idReplyMessage = idReplyMessage;
    }

    /**
     * @return message's text
     */
    public String getText() {
        return text;
    }

    /**
     * @return message's date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @return message's idUserFrom
     */
    public Long getIdUserFrom() {
        return idUserFrom;
    }

    /**
     * @return message's idChat
     */
    public Long getIdChat() {
        return idChat;
    }

    /**
     * @return message's idReplyMessage
     */
    public Long getIdReplyMessage() {
        return idReplyMessage;
    }

    /**
     * @return a String with the attributes of a Message
     */
    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", date=" + date +
                ", idUserFrom=" + idUserFrom +
                ", idChat=" + idChat +
                ", idReplyMessage=" + idReplyMessage +
                '}';
    }
}
