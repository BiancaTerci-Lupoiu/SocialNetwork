package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MessageDTO extends Entity<Long>{

    private String text;
    private LocalDateTime date;
    private User userFrom;
    private MessageDTO replyMessage;
    private Map<Long,User> usersTo;

    public MessageDTO(String text, LocalDateTime date, User userFrom) {
        this.text = text;
        this.date = date;
        this.userFrom = userFrom;
        usersTo=new HashMap<>();
        replyMessage=null;
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
}
