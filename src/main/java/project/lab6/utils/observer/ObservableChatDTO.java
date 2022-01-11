package project.lab6.utils.observer;

import project.lab6.domain.dtos.ChatDTO;

public class ObservableChatDTO extends ObservableImplementation<ChatDTO> {
    private ChatDTO chat;

    public ObservableChatDTO(ChatDTO chat) {
        this.chat = chat;
    }

    public ChatDTO getChat() {
        return chat;
    }

    public void setChat(ChatDTO chat) {
        this.chat = chat;
        notifyObservers(chat);
    }
}
