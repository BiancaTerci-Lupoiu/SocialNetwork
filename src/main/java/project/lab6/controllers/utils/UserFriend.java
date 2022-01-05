package project.lab6.controllers.utils;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

/**
 * entity for the friends and requests table from the GUI
 */
public class UserFriend {
    private String firstName;
    private String lastName;
    private LocalDate date;
    private Long id;
    private Button button1;
    private Button button2;

    public UserFriend(Long id, String firstName, String lastName, LocalDate date, Button button) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.button1 = button;
    }

    public UserFriend(Long id, String firstName, String lastName, LocalDate date, Button button, Button button2) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.button1 = button;
        this.button2 = button2;
    }

    /**
     * @return the firstName of the UserFriends
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * sets the firstName of the UserFriend to firstName
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName of the UserFriend
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * sets the lastName of the UserFriend to lastName
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the date of the UserFriend
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * sets the date of the UserFriend to date
     *
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * @return the id of the UserFriend
     */
    public Long getId() {
        return id;
    }

    /**
     * sets the id of the UserFriend to id
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the button1 of the UserFriend
     */
    public Button getButton1() {
        return button1;
    }

    /**
     * sets the button1 of the UserFriends to unfriendButton
     *
     * @param unfriendButton
     */
    public void setButton1(Button unfriendButton) {
        this.button1 = unfriendButton;
    }

    /**
     * @return the button2 of the UserFriend
     */
    public Button getButton2() {
        return button2;
    }

    /**
     * sets the button2 of the UserFriend to button2
     *
     * @param button2
     */
    public void setButton2(Button button2) {
        this.button2 = button2;
    }
}
