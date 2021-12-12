package project.lab6.domain.chat;

import project.lab6.domain.Entity;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private final String text;
    private final LocalDateTime date;
    private final Long idUserFrom;
    private final Long idChat;
    private final Long idReplyMessage;

    public Message(String text, LocalDateTime date, Long idUserFrom, Long idChat, Long idReplyMessage) {
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idChat = idChat;
        this.idReplyMessage = idReplyMessage;
    }

    public Message(Long id, String text, LocalDateTime date, Long idUserFrom, Long idChat, Long idReplyMessage) {
        setId(id);
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idChat = idChat;
        this.idReplyMessage = idReplyMessage;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getIdUserFrom() {
        return idUserFrom;
    }

    public Long getIdChat() {
        return idChat;
    }

    public Long getIdReplyMessage() {
        return idReplyMessage;
    }

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
