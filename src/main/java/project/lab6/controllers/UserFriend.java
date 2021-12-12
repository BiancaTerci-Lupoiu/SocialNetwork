package project.lab6.controllers;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

public class UserFriend {
    private String firstName;
    private String lastName;
    private LocalDate date;
    private Long id;
    private Button unfriendButton;

    public UserFriend(Long id, String firstName, String lastName, LocalDate date, Button button) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.unfriendButton = button;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Button getUnfriendButton() {
        return unfriendButton;
    }

    public void setUnfriendButton(Button unfriendButton) {
        this.unfriendButton = unfriendButton;
    }
}
