package project.lab6.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Message extends Entity<Long> {
    private String text;
    private LocalDateTime date;
    private Long idUserFrom;
    // the id of the message to which we reply
    private Long idReplyMessage;
    private List<Long> idUsersTo;

    /**
     * constructor with the list of idUsersTo
     *
     * @param text
     * @param date
     * @param idUserFrom
     * @param idUsersTo
     */
    public Message(String text, LocalDateTime date, Long idUserFrom, List<Long> idUsersTo) {
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idUsersTo = idUsersTo;
        idReplyMessage = null;
    }

    /**
     * constructor with the list of idUsersTo= null
     *
     * @param text
     * @param date
     * @param idUserFrom
     */
    public Message(String text, LocalDateTime date, Long idUserFrom) {
        this(text, date, idUserFrom, new ArrayList<>());
    }

    /**
     * adds an id of an user to the idUsersTo list
     *
     * @param idUser
     */
    public void addIdUserTo(Long idUser) {
        idUsersTo.add(idUser);
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
     * @return message's idReplyMessage
     */
    public Long getIdReplyMessage() {
        return idReplyMessage;
    }

    /**
     * @return message's list of idUsersTo (recipients)
     */
    public List<Long> getIdUsersTo() {
        return idUsersTo;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * sets the date of the message with the value date
     *
     * @param date
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setIdUserFrom(Long idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    /**
     * sets the idReplyMessage of the message with the value idReplyMessage
     *
     * @param idReplyMessage
     */
    public void setIdReplyMessage(Long idReplyMessage) {
        this.idReplyMessage = idReplyMessage;
    }

    public void setIdUsersTo(List<Long> idUsersTo) {
        this.idUsersTo = idUsersTo;
    }

    /**
     * @return the Message entity as a String
     */
    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", date=" + date +
                ", idUserFrom=" + idUserFrom +
                ", idReplyMessage=" + idReplyMessage +
                ", idUsersTo=" + idUsersTo +
                '}';
    }
}
