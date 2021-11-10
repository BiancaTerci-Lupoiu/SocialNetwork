package socialnetwork.domain;

// id-ul prieteniei este un tuplu format din <id_user1,id_user2>


public class Friendship extends Entity<Tuple<Long, Long>> {
    /**
     * constructor that sets the id of the entity
     *
     * @param id1 the id of the first user from friendship
     * @param id2 the id of the second user from friendship
     */
    public Friendship(Long id1, Long id2) {
        super.setId(new Tuple<Long, Long>(id1, id2));
    }

    /**
     * @return the friendship entity as string
     */
    @Override
    public String toString() {
        return "Friendship{" + "idUser1=" + getId().getLeft() + " , " + "idUser2=" + getId().getRight() + "}";
    }

}
