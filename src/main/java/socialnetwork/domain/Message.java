package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;


public class Message extends Entity<Long>{
    private String text;
    private LocalDateTime date;
    private Long idUserFrom;
    private Long idReplyMessage;
    private List<Long> idUsersTo;

    public Message(String text, LocalDateTime date, Long idUserFrom, List<Long> idUsersTo) {
        this.text = text;
        this.date = date;
        this.idUserFrom = idUserFrom;
        this.idUsersTo = idUsersTo;
        idReplyMessage=null;
    }

    public Message(String text, LocalDateTime date, Long idUserFrom) {
        this(text,date,idUserFrom,new ArrayList<>());
    }

    public void addIdUserTo(Long idUser){
        idUsersTo.add(idUser);
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

    public Long getIdReplyMessage() {
        return idReplyMessage;
    }

    public List<Long> getIdUsersTo() {
        return idUsersTo;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setIdUserFrom(Long idUserFrom) {
        this.idUserFrom = idUserFrom;
    }

    public void setIdReplyMessage(Long idReplyMessage) {
        this.idReplyMessage = idReplyMessage;
    }

    public void setIdUsersTo(List<Long> idUsersTo) {
        this.idUsersTo = idUsersTo;
    }
}
