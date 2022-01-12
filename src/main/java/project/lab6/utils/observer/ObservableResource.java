package project.lab6.utils.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * A class created for observing of a resource that can be changed at any time
 *
 * @param <Resource> the resource that needs to be observed
 */
public class ObservableResource<Resource> implements Observable<Resource> {
    private Resource resource;
    Set<Observer<Resource>> observerSet = new HashSet<>();

    public ObservableResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void addObserver(Observer<Resource> observer) {
        observerSet.add(observer);
    }

    @Override
    public void removeObserver(Observer<Resource> observer) {
        observerSet.remove(observer);
    }

    @Override
    public void notifyObservers(Resource newValue) {
        for (var observer : observerSet)
            observer.update(newValue);
    }

    /**
     * @return The resource that is being observed
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * Sets the resource to a new value
     * The modification will be notified to all observers
     */
    public void setResource(Resource resource) {
        this.resource = resource;
        notifyObservers(resource);
    }
}
