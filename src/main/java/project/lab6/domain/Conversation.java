package project.lab6.domain;

import project.lab6.domain.dtos.MessageDTO;

import java.util.List;

public class Conversation {
    List<MessageDTO> messagesList;

    /**
     * constructor
     * @param messagesList the list with messages
     */
    public Conversation(List<MessageDTO> messagesList) {
        this.messagesList = messagesList;
    }

    /**
     *
     * @return the list of messages
     */
    public List<MessageDTO> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageDTO> messagesList) {
        this.messagesList = messagesList;
    }

    /**
     *
     * @return the list of messages as a String (conversation between 2 users)
     */
    @Override
    public String toString() {
        String conversationString = "Conversation:\n";
        for (MessageDTO message : messagesList) {
            conversationString += message.toString() + "\n";
        }
        return conversationString;
    }
}
