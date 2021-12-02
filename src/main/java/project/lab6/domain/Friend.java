package project.lab6.domain;

import project.lab6.utils.Constants;

import java.time.LocalDate;

public class Friend{
    private User user;
    private LocalDate date;
    private DirectedStatus status;

    /**
     *
     * @param user the user to whom the request was sent to
     * @param date the date when the friendship started
     * @param status the status of the friendship request
     */
    public Friend(User user,LocalDate date, DirectedStatus status){
        this.user=user;
        this.date=date;
        this.status=status;
    }
    /**
     *
     * @return the date of the friendship
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
    public void setStatus(DirectedStatus status) {
        this.status = status;
    }

    /**
     *
     * @return the status of the friendship
     */
    public DirectedStatus getStatus() {
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
     * @return the friend
     */
    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return user+
                ", date: " + date.format(Constants.DATE_FORMATTER)+
                ", status=" + status;
    }
}