package project.lab6.domain.entities;

import javafx.scene.image.Image;
import project.lab6.domain.DirectedStatus;
import project.lab6.domain.Friend;
import project.lab6.utils.Constants;
import project.lab6.utils.Images;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User extends Entity<Long> {
    private final String email;
    private final Map<Long, Friend> friends;
    private final String hashPassword;
    private final String salt;
    private String firstName;
    private String lastName;

    /**
     * constructor
     *
     * @param id           Long of the user
     * @param email        String of the user
     * @param firstName    String of the user
     * @param lastName     String  of the user
     * @param hashPassword String of the user
     * @param salt         String of the user
     */
    public User(Long id, String email, String firstName, String lastName, String hashPassword, String salt) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashPassword = hashPassword;
        this.salt = salt;
        friends = new HashMap<>();
        setId(id);
    }

    /**
     * constructor
     *
     * @param email        String of the user
     * @param firstName    String of the user
     * @param lastName     String  of the user
     * @param hashPassword String of the user
     * @param salt         String of the user
     */
    public User(String email, String firstName, String lastName, String hashPassword, String salt) {
        this(null, email, firstName, lastName, hashPassword, salt);
    }

    /**
     * @return user's firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the firstName of the user to firstName
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return user's lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the lastName of the user to lastName
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return user's hashPassword
     */
    public String getHashPassword() {
        return hashPassword;
    }

    /**
     * @return user's salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @return users's friends
     */
    public Collection<Friend> getFriends() {
        return friends.values();
    }

    /**
     * @param idUser
     * @return true if the user has the friend with id=idUser, false otherwise
     */
    public boolean findFriend(Long idUser) {
        return friends.get(idUser) != null;
    }

    /**
     * @param status
     * @return the friends of the user with some status=status
     */
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
        return String.format("(%s) %s %s", getId(), getFirstName(), getLastName());
    }

    public String toStringWithFriends() {
        return String.format("%s, friends=%s", this, getFriends());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    /**
     * @return user's profile picture
     */
    public Image getImage() {
        return Images.getImage("users", Constants.PATH_DEFAULT_USER_IMAGE, getId());
    }

    /**
     * saves user's profile picture
     * @param imagePath the path to the image we want to save
     * @throws IOException if the path is not a valid path
     */
    public void saveImage(String imagePath) throws IOException {
        Images.saveImage("users", getId(), imagePath);
    }

    /**
     * deletes user's profile picture
     * @throws IOException
     */
    public void deleteImage() throws IOException {
        Images.deleteImage("users", getId());
    }
}
