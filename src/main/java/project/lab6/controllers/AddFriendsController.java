package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.control.skin.TableViewSkinBase;
import javafx.scene.input.InputMethodEvent;
import project.lab6.domain.Status;
import project.lab6.domain.User;
import project.lab6.domain.UserRecord;
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

    private User loggedUser;
    private Service service;

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    public void setService(Service service){
        this.service=service;
    }

    @FXML
    public void initialize() {
        //addFriendsTableView.setVisible(false);
        nameColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        addFriendColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        nameColumn.setCellValueFactory(new PropertyValueFactory<UserRecord,String>("name"));
        addFriendColumn.setCellValueFactory(new PropertyValueFactory<UserRecord,Button>("addButton"));
        userNameTextField.textProperty().addListener((obs, oldText, newText) ->findUserByName());

    }

    private Button createAddButton(Long idUserTo){
        Button addFriendButton=new Button();
        addFriendButton.setText("+");
        addFriendButton.setPrefWidth(20);
        addFriendButton.setPrefHeight(20);
        addFriendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                service.addFriendship(loggedUser.getId(),idUserTo, LocalDate.now(), Status.PENDING);
            }
        });

        return addFriendButton;
    }

    public void findUserByName() {
        //List<User> usersList = service.searchUsersByNameNotFriendsWithLoggedUser(loggedUser,userNameTextField.toString());
        List<User> usersList=service.getAllUsers().values().stream().toList();
        System.out.println(usersList.size());
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addFriendButton=createAddButton(user.getId());
            UserRecord userRecord=new UserRecord(user.getId(),name,addFriendButton);

            addFriendsTableView.getItems().add(userRecord);
        }
    }
}
