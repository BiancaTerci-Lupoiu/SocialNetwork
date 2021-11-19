package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageDto extends Entity<Long>{

    private String text;
    private LocalDateTime date;
    private User userFrom;
    private MessageDto replyMessage;
    private Map<Long,User> usersTo;

    public MessageDto(String text, LocalDateTime date, User userFrom) {
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

    public MessageDto getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(MessageDto replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Map<Long, User> getUsersTo() {
        return usersTo;
    }

    public void setUsersTo(Map<Long, User> usersTo) {
        this.usersTo = usersTo;
    }
}
