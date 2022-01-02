package project.lab6.utils.observer;

import java.util.HashSet;
import java.util.Set;

public class ObservableImplementation<T> implements Observable<T> {
    private final Set<Observer<T>> observers;
    public ObservableImplementation()
    {
        observers = new HashSet<>();
    }
    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(T newValue) {
        for(Observer<T> observer : observers)
            observer.update(newValue);
    }
}