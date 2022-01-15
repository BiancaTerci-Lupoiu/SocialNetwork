package project.lab6.domain.dtos;

import project.lab6.domain.entities.User;

import java.time.LocalDateTime;

public class EventForUserDTO {
    private final Long id;
    private final LocalDateTime date;
    private final String title;
    private final String description;
    private final User owner;
    private boolean subscribed;

    /**
     * constructor
     * @param id
     * @param date
     * @param title
     * @param description
     * @param owner
     * @param subscribed
     */
    public EventForUserDTO(Long id, LocalDateTime date, String title, String description, User owner, boolean subscribed) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.subscribed = subscribed;
    }

    /**
     * @return event's id
     */
    public Long getId() {
        return id;
    }
    /**
     * @return event's date
     */
    public LocalDateTime getDate() {
        return date;
    }
    /**
     * @return event's title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @return event's description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return event's user owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @return true if the user is subscribed to this event and false otherwise
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * sets the subscribed status of the event to the value subscribed
     * @param subscribed
     */
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
