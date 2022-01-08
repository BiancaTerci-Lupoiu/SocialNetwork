package project.lab6.domain.entities.events;

import project.lab6.domain.entities.Entity;

import java.time.LocalDateTime;

public class Event extends Entity<Long> {
    private final Long idUserOwner;
    private final String title;
    private final String description;
    private final LocalDateTime date;

    public Event(Long id, Long idUserOwner, String title, String description, LocalDateTime date) {
        setId(id);
        this.idUserOwner = idUserOwner;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Event(Long idUserOwner, String title, String description, LocalDateTime date) {
        this(null, idUserOwner, title, description, date);
    }

    public Long getIdUserOwner() {
        return idUserOwner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
