package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.service.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FriendsController{
    ObservableList<UserFriend> modelFriends = FXCollections.observableArrayList();
    private Service service;
    private Long id;

    public FriendsController() {

    }

    @FXML
    TableColumn<UserFriend, String> firstName;
    @FXML
    TableColumn<UserFriend, String> lastName;
    @FXML
    TableColumn<UserFriend, Date> date;
    @FXML
    TableColumn<UserFriend, Button> button;
    @FXML
    TableView<UserFriend> tableViewFriends;

    @FXML
    public void initialize() {
        firstName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("firstName")));
        lastName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("lastName")));
        date.setCellValueFactory((new PropertyValueFactory<UserFriend, Date>("date")));
        button.setCellValueFactory((new PropertyValueFactory<UserFriend, Button>("unfriendButton")));


        tableViewFriends.setItems(modelFriends);
    }

    private List<UserFriend> getFriendsList() {
        return service.getFriends(1L)
                .stream()
                .map(n -> new UserFriend(n.getUser().getId(),
                        n.getUser().getFirstName(),
                        n.getUser().getLastName(),
                        n.getDate(),
                        createUnfriendButton(n.getUser().getId()))).toList();

    }

    private Button createUnfriendButton(Long idFriend) {
        Button addUnfriendButton = new Button();
        addUnfriendButton.setText("Unfriend");
        addUnfriendButton.setPrefWidth(30);
        addUnfriendButton.setPrefHeight(20);
        addUnfriendButton.setOnAction(event -> {
                    service.deleteFriendship(1L, idFriend);
                    modelFriends.setAll(getFriendsList());
                }
        );
        return addUnfriendButton;
    }

    public void setService(Service service) {
        this.service = service;
        modelFriends.setAll(getFriendsList());
    }
}
