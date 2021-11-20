package socialnetwork.domain;

import java.util.*;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private Map<Long, Friend> friends;

    /**
     * constructor
     *
     * @param firstName of the user
     * @param lastName  of the user
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new HashMap<>();
    }

    /**
     * @return user's firstName
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return user's lastName
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return users's friends
     */
    public Collection<Friend> getFriends() {
        return friends.values();
    }

    public Collection<Friend> getFriends(DirectedStatus status) {
        return friends.values().stream()
                .filter(x -> x.getStatus() == status).toList();
    }

    /**
     * add a friend for the user
     *
     * @param friend -the new friend to be added
     */
    public void addFriend(Friend friend) {
        friends.put(friend.getUser().getId(), friend);
    }

    /**
     * removes the friend with the specified id
     *
     * @param id - friend id to be removed
     */
    public void removeFriend(Long id) {
        friends.remove(id);
    }

    /**
     * @return the User entity as String
     */
    @Override
    public String toString() {
        //TODO: Sa afisam de aici doar prieteniile cu statusul approved?
        String friendList = "[";
        for (var friend : friends.values())
            if(friend.getStatus() == DirectedStatus.APPROVED)
                friendList += friend.getUser().getFirstName() + " , " + friend.getUser().getLastName() + " ; ";
        friendList += "]";
        return "User{" +
                "id='" + getId() + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friendList +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

}
