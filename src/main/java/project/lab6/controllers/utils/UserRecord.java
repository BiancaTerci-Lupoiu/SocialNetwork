package project.lab6.controllers.utils;

import javafx.scene.control.Button;

import java.util.Objects;

/**
 * entity for the addFriends table from the GUI
 */
public class UserRecord {
    private String name;
    private Button addButton;
    private Long id;

    /**
     * constructor
     *
     * @param id
     * @param name
     * @param addButton
     */
    public UserRecord(Long id, String name, Button addButton) {
        this.id = id;
        this.name = name;
        this.addButton = addButton;
    }

    /**
     * @return the id of the UserRecord
     */
    public Long getId() {
        return id;
    }

    /**
     * sets the id of the UserRecord to id
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name of the UserRecord
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name of the UserRecord to name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the addButton of the UserRecord
     */
    public Button getAddButton() {
        return addButton;
    }

    /**
     * sets the addButton of the user record to addButton
     *
     * @param addButton
     */
    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRecord that = (UserRecord) o;
        return Objects.equals(name, that.name) && Objects.equals(addButton, that.addButton) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, addButton, id);
    }
}
