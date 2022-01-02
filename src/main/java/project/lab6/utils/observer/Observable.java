package project.lab6.utils.observer;

import javafx.collections.ObservableList;
import project.lab6.domain.dtos.ChatDTO;

public interface Observable<T>{
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);
    void notifyObservers(T newValue);
}
