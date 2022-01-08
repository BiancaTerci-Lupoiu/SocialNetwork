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

    public EventForUserDTO(Long id, LocalDateTime date, String title, String description, User owner, boolean subscribed) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.subscribed = subscribed;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }

    /**
     * @return true if the user is subscribed to this event and false otherwise
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
