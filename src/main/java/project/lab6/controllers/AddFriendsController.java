package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.domain.Status;
import project.lab6.domain.User;
import project.lab6.service.Service;

import java.time.LocalDate;
import java.util.List;

public class AddFriendsController {

    @FXML
    private TextField userNameTextField;
    @FXML
    private TableView<UserRecord> addFriendsTableView;
    @FXML
    private TableColumn<UserRecord, String> nameColumn;
    @FXML
    private TableColumn<UserRecord, Button> addFriendColumn;

    private Long idLoggedUser;
    private Service service;

    public void setLoggedUser(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        //addFriendsTableView.setVisible(false);
        nameColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        addFriendColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        nameColumn.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addFriendColumn.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        userNameTextField.textProperty().addListener((obs, oldText, newText) -> findUserByName());

    }

    private Button createAddButton(Long idUserTo) {
        Button addFriendButton = new Button();
        addFriendButton.setText("+");
        addFriendButton.setPrefWidth(30);
        addFriendButton.setPrefHeight(30);
        addFriendButton.setOnAction(event -> {
            service.addFriendship(idLoggedUser, idUserTo, LocalDate.now(), Status.PENDING);
            findUserByName();

        });

        return addFriendButton;
    }

    public void findUserByName() {
        addFriendsTableView.getItems().clear();
        List<User> usersList = service.searchUsersByNameNotFriendsWithLoggedUser(service.getUserWithFriends(idLoggedUser), userNameTextField.getText());
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addFriendButton = createAddButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addFriendButton);

            addFriendsTableView.getItems().add(userRecord);
        }
    }
}
