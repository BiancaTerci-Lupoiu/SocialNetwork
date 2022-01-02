package project.lab6.utils.observer;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import project.lab6.domain.dtos.ChatDTO;

import java.util.ArrayList;
import java.util.List;

public class ObservableChatDTO extends ObservableImplementation<ChatDTO> {
    private ChatDTO chat;
    private final List<ObservableList<ChatDTO>> observers;

    public ObservableChatDTO(ChatDTO chat) {
        this.chat = chat;
        observers = new ArrayList<>();
    }

    /**
     * Adds an observableList in which this chatDTO is observed
     * @param observableList the ObservableList in which the chat is observed
     */
    public void addObservableList(ObservableList<ChatDTO> observableList) {
        observers.add(observableList);
    }

    public ChatDTO getChat() {
        return chat;
    }

    public void setChat(ChatDTO chat) {
        this.chat = chat;
        for (var observer : observers) {
            FilteredList<ChatDTO> filteredList = observer.filtered(chatDTO -> chatDTO.getIdChat().equals(chat.getIdChat()));
            int index = filteredList.getSourceIndex(0);
            observer.set(index, chat);
        }
        notifyObservers(chat);
    }
}
