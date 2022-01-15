package project.lab6.domain.entities.events;

import project.lab6.domain.entities.Entity;

import java.time.LocalDateTime;

public class Event extends Entity<Long> {
    private final Long idUserOwner;
    private final String title;
    private final String description;
    private final LocalDateTime date;

    /**
     * constructor with event's id
     * @param id
     * @param idUserOwner
     * @param title
     * @param description
     * @param date
     */
    public Event(Long id, Long idUserOwner, String title, String description, LocalDateTime date) {
        setId(id);
        this.idUserOwner = idUserOwner;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    /**
     * constructor without the event's id
     * @param idUserOwner
     * @param title
     * @param description
     * @param date
     */
    public Event(Long idUserOwner, String title, String description, LocalDateTime date) {
        this(null, idUserOwner, title, description, date);
    }

    /**
     * @return the id of the owner user of the event
     */
    public Long getIdUserOwner() {
        return idUserOwner;
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
     * @return event's date
     */
    public LocalDateTime getDate() {
        return date;
    }
}
