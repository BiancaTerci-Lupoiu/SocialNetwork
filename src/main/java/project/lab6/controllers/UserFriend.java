package project.lab6.controllers;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

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

    public Button getButton1() {
        return button1;
    }

    public void setButton1(Button unfriendButton) {
        this.button1 = unfriendButton;
    }

    public Button getButton2() {
        return button2;
    }

    public void setButton2(Button button2) {
        this.button2 = button2;
    }
}
