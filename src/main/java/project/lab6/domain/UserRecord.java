package project.lab6.domain;

import javafx.scene.control.Button;

public class UserRecord {
    private String name;
    private Button addButton;
    private Long id;

    public UserRecord(Long id,String name, Button addButton) {
        this.id=id;
        this.name = name;
        this.addButton = addButton;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Button getAddButton() {
        return addButton;
    }

    public void setAddButton(Button addButton) {
        this.addButton = addButton;
    }
}
