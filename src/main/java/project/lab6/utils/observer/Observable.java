package project.lab6.utils.observer;

public interface Observable<T> {
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notifyObservers(T newValue);
}
