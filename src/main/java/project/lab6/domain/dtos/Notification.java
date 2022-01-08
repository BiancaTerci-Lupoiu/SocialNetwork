package project.lab6.domain.dtos;

import project.lab6.domain.NotifyTime;

import java.time.LocalDateTime;

public class Notification {
    private final EventForUserDTO forEvent;
    private final NotifyTime time;

    public Notification(EventForUserDTO forEvent, NotifyTime time) {
        this.forEvent = forEvent;
        this.time = time;
    }

    /**
     * @return The event for which the notification is shown
     */
    public EventForUserDTO getForEvent() {
        return forEvent;
    }

    /**
     * @return the message of the notification
     */
    public String getMessage() {
        return time.getMessage(forEvent);
    }

    public LocalDateTime getTimeOfNotifying() {
        return forEvent.getDate().minus(time.getDifferenceInTime());
    }
}
