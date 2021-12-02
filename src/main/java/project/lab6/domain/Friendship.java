package project.lab6.domain;

// id-ul prieteniei este un tuplu format din <id_user1,id_user2>

import java.time.LocalDate;

public class Friendship extends Entity<Tuple<Long, Long>> {
    private LocalDate date;
    private Status status;

    /**
     * constructor that sets the id of the entity
     *
     * @param id1    the id of the first user from friendship
     * @param id2    the id of the second user from friendship
     * @param date   the date this friendship was made
     * @param status the status of this friendship
     */
    public Friendship(Long id1, Long id2, LocalDate date, Status status) {
        super.setId(new Tuple<Long, Long>(id1, id2));
        this.status = status;
        this.date = date;
    }

    /**
     * @return the friendship entity as string
     */
    @Override
    public String toString() {
        return "Friendship{" + "idUser1=" + getId().getLeft() + " , " + "idUser2=" + getId().getRight() + "}";
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
