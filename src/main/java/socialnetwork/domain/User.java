package socialnetwork.domain;

import java.util.*;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private Map<Long, User> friends;

    /**
     * constructor
     *
     * @param firstName of the user
     * @param lastName  of the user
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new HashMap<Long, User>();
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
    public Iterable<User> getFriends() {
        return friends.values();
    }

    public void setFriends(Map<Long, User> friends) {
        this.friends = friends;
    }

    /**
     * add a friend for the user
     *
     * @param user -the new friend to be added
     */
    public void addFriend(User user) {
        friends.put(user.getId(), user);
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
        String friendList = "[";
        for (Map.Entry<Long, User> pair : friends.entrySet())
            friendList += pair.getValue().getFirstName() + " , " + pair.getValue().getLastName() + " ; ";
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
