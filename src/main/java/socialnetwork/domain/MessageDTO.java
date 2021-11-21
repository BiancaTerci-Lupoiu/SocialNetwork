package socialnetwork.domain;

import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageDTO extends Entity<Long>{

    private String text;
    private LocalDateTime date;
    private User userFrom;
    private MessageDTO replyMessage;
    private Map<Long,User> usersTo;

    public MessageDTO(String text, LocalDateTime date, User userFrom,MessageDTO replyMessage,Map<Long,User> usersTo) {
        this.text = text;
        this.date = date;
        this.userFrom = userFrom;
        this.replyMessage=replyMessage;
        this.usersTo=usersTo;
    }

    public void addUserTo(User user){
        usersTo.put(user.getId(), user);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public MessageDTO getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(MessageDTO replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Iterable<User> getUsersTo() {
        return usersTo.values();
    }

    public void setUsersTo(Map<Long, User> usersTo) {
        this.usersTo = usersTo;
    }

    /**
     *
     * @param idUser
     * @return the user with id=idUser from the usersTo list, or null if there is no user with the
     *          specified id
     */
    public User getUserToById(Long idUser){
        return usersTo.get(idUser);
    }

    // (idMesaj) Nume Prenume:... mesaj:... (data+ora)
    @Override
    public String toString() {
        String messageToString="("+getId()+") "+ userFrom.getLastName()+" "+userFrom.getFirstName()+" ";
        if(replyMessage!=null)
            messageToString+="Replied to "+"\""+replyMessage.getText()+"\""+"\n"+"\t\t";
        messageToString+=getText()+" ("+getDate().format(Constants.DATETIME_FORMATTER)+") ";
        return messageToString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(text, that.text) && Objects.equals(date, that.date) && Objects.equals(userFrom, that.userFrom) && Objects.equals(replyMessage, that.replyMessage) && Objects.equals(usersTo, that.usersTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, date, userFrom, replyMessage, usersTo);
    }
}
