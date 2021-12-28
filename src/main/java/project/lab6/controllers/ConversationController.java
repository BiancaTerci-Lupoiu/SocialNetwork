package project.lab6.controllers;

import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterServiceMessages;

public class ConversationController implements SetterServiceMessages {

    private Long idChat;
    private ServiceMessages serviceMessages;

    public void setIdChat(Long idChat){
        this.idChat=idChat;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages=serviceMessages;
    }
}
