package project.lab6.domain;

import project.lab6.domain.dtos.EventForUserDTO;

import java.time.Duration;

public enum NotifyTime {
    STARTED("%s just started!", Duration.ofSeconds(5)),
    ONE_HOUR("%s will start shortly", Duration.ofHours(1)),
    ONE_DAY("Get ready for tomorrows event: %s", Duration.ofDays(1));

    private final String message;
    private final Duration differenceInTime;

    NotifyTime(String message, Duration differenceInTime) {
        this.message = message;
        this.differenceInTime = differenceInTime;
    }

    /**
     * @return the difference in time between when the event start and when should the user be notified
     */
    public Duration getDifferenceInTime() {
        return differenceInTime;
    }

    /**
     * @param eventForUserDTO
     * @return the specific notification message for the given event
     */
    public String getMessage(EventForUserDTO eventForUserDTO) {
        return String.format(message, eventForUserDTO.getTitle());
    }
}
