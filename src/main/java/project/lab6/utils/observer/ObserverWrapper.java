package project.lab6.utils.observer;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import project.lab6.domain.dtos.ChatDTO;

import java.util.Comparator;

public class ObserverWrapper {
    private ObserverWrapper() {
    }

    private static <T> Observer<T> fromObservableListGeneric(ObservableList<T> observableList, Comparator<T> comparator) {
        return new Observer<T>() {
            @Override
            public void update(T newValue) {
                FilteredList<T> filteredList = observableList.filtered(value -> comparator.compare(value, newValue) == 0);
                if (filteredList.isEmpty())
                    return;
                int index = filteredList.getSourceIndex(0);
                observableList.set(index, newValue);
            }
        };
    }

    public static Observer<ChatDTO> fromObservableList(ObservableList<ChatDTO> chatObservableList) {
        return fromObservableListGeneric(chatObservableList, Comparator.comparing(ChatDTO::getIdChat));
    }
}
