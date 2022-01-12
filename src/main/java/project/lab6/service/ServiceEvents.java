package project.lab6.service;

import javafx.scene.image.Image;
import project.lab6.domain.NotifyTime;
import project.lab6.domain.TupleWithIdUserEvent;
import project.lab6.domain.dtos.EventForUserDTO;
import project.lab6.domain.dtos.EventsList;
import project.lab6.domain.dtos.Notification;
import project.lab6.domain.entities.User;
import project.lab6.domain.entities.events.Event;
import project.lab6.domain.entities.events.Subscription;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.repointerface.Repository;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServiceEvents {
    private final Repository<Long, User> repoUsers;
    private final Repository<Long, Event> repoEvents;
    private final Repository<TupleWithIdUserEvent, Subscription> repoSubscription;
    private final Validator<Event> validatorEvent;
    private final Validator<Subscription> validatorSubscription;

    public ServiceEvents(Repository<Long, User> repoUsers, Repository<Long, Event> repoEvents, Repository<TupleWithIdUserEvent, Subscription> repoSubscription, Validator<Event> validatorEvent, Validator<Subscription> validatorSubscription) {
        this.repoUsers = repoUsers;
        this.repoEvents = repoEvents;
        this.repoSubscription = repoSubscription;
        this.validatorEvent = validatorEvent;
        this.validatorSubscription = validatorSubscription;
    }

    public void createEvent(Long idLoggedUser, String title, String description, LocalDateTime date) {
        Event event = new Event(idLoggedUser, title, description, date);
        validatorEvent.validate(event);
        event = repoEvents.save(event);
        if (event == null)
            throw new ServiceException("The event could not be saved!");
        subscribe(idLoggedUser, event.getId(), date);
    }

    public void modifyEvent(Long idEvent, String newTitle, String newDescription, LocalDateTime newDate) {
        Event event = repoEvents.findOne(idEvent);
        if (event == null)
            throw new ServiceException("Can't find specified event!");
        Event newEvent = new Event(idEvent, event.getIdUserOwner(), newTitle, newDescription, newDate);
        if (!repoEvents.update(newEvent))
            throw new ServiceException("Can't modify the event!");
    }

    private EventForUserDTO getEventDTO(Event event, boolean isSubscribed) {
        User owner = repoUsers.findOne(event.getIdUserOwner());
        return new EventForUserDTO(event.getId(), event.getDate(), event.getTitle(), event.getDescription(), owner, isSubscribed);
    }

    /**
     * Returns all the events for a specified user
     *
     * @param idUser the id of the user for who you want to show the events
     */
    public EventsList getEventsForUser(Long idUser) {
        Map<Long, EventForUserDTO> events = repoEvents.findAll().stream()
                .map(event -> getEventDTO(event, false))
                .collect(Collectors.toMap(EventForUserDTO::getId, eventForUserDTO -> eventForUserDTO));

        repoSubscription.findAll().stream()
                .filter(subscription -> subscription.getIdUser().equals(idUser))
                .forEach(subscription -> {
                    Long idEvent = subscription.getIdEvent();
                    events.get(idEvent).setSubscribed(true);
                });
        Collection<EventForUserDTO> sortedAfterSubscribedFirst = events.values().stream()
                .sorted((eventA, eventB) ->
                {
                    boolean subscribedA = eventA.isSubscribed();
                    boolean subscribedB = eventB.isSubscribed();
                    if (subscribedA == subscribedB)
                        return 0;
                    if (subscribedA)
                        return 1;
                    return -1;
                })
                .toList();
        return new EventsList(idUser, sortedAfterSubscribedFirst);
    }

    public void subscribe(Long idLoggedUser, Long idEvent, LocalDateTime date) {
        Subscription subscription = new Subscription(idLoggedUser, idEvent, date);
        validatorSubscription.validate(subscription);
        subscription = repoSubscription.save(subscription);
        if (subscription == null)
            throw new ServiceException("Can't create subscription!");
    }

    public void unsubscribe(Long idLoggedUser, Long idEvent) {
        if (!repoSubscription.delete(new TupleWithIdUserEvent(idLoggedUser, idEvent)))
            throw new ServiceException("Can't unsubscribe from event!");
    }

    public List<Notification> getNotification(Long idUser) {
        List<Notification> notifications = new ArrayList<>();
        for (var subscription : repoSubscription.findAll()) {
            if (!subscription.getIdUser().equals(idUser))
                continue;
            Event event = repoEvents.findOne(subscription.getIdEvent());
            LocalDateTime now = LocalDateTime.now();
            for (var notifyTime : NotifyTime.values()) {
                LocalDateTime notifyDate = event.getDate()
                        .minus(notifyTime.getDifferenceInTime());
                if (notifyDate.isAfter(subscription.getDate()))
                    continue;
                if (notifyDate.isBefore(now)) {
                    Notification notification = new Notification(getEventDTO(event, true), notifyTime);
                    notifications.add(notification);
                }
            }
        }
        return notifications.stream().sorted((a, b) ->
                {
                    LocalDateTime timeA = a.getTimeOfNotifying();
                    LocalDateTime timeB = b.getTimeOfNotifying();
                    if (timeA.isAfter(timeB))
                        return 1;
                    if (timeB.isAfter(timeA))
                        return -1;
                    return 0;
                })
                .toList();
    }
}
