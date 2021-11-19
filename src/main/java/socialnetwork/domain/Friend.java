package socialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

import socialnetwork.domain.User;

public class Friend{
    private User user;
    private LocalDate date;
    private Status status;

    /**
     *
     * @param user the user to whom the request was sent to
     * @param date the date when the friendship started
     * @param status the status of the friendship request
     */
    public Friend(User user,LocalDate date, Status status){
        this.user=user;
        this.date=date;
        this.status=status;

    }
    /**
     *
     * @returns the date of the friendship
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     *
     * @param status sets the status of the friendship
     * */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     *
     * @returns the status of the friendship
     */
    public Status getStatus() {
        return status;
    }

    /**
     *
     * @param user sets the friend
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @returns the friend
     */
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user" + user+
                ", date" + date+
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        Friend friend = (Friend) o;
        return Objects.equals(user, friend.getUser()) && Objects.equals(date, friend.getDate()) && status == friend.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, date, status);
    }
}