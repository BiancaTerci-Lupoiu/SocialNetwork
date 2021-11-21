package socialnetwork.domain;

import java.util.List;

public class Conversation {
    List<MessageDTO> messagesList;

    public Conversation(List<MessageDTO> messagesList) {
        this.messagesList = messagesList;
    }

    public List<MessageDTO> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageDTO> messagesList) {
        this.messagesList = messagesList;
    }

    @Override
    public String toString() {
        String conversationString="Conversation:\n";
        for(MessageDTO message:messagesList){
            conversationString+= message.toString()+"\n";
        }
        return conversationString;
    }
}
