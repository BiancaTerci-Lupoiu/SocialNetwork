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
import project.lab6.domain.Friend;
import project.lab6.domain.Friendship;
import project.lab6.service.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FriendsController {
    ObservableList<Friend> modelFriends= FXCollections.observableArrayList();
    private Service service;
    private Long id;

    public FriendsController(Long idUser,Service service){
        this.id=idUser;
        this.service=service;
    }
    public FriendsController(){

    }
    @FXML
    TableColumn<Friend,String> firstName;
    @FXML
    TableColumn<Friend,String> lastName;
    @FXML
    TableColumn<Friend, Date> date;
    @FXML
    TableView<Friend> tableViewFriends;

    @FXML
    public void initialize(){
        firstName.setCellValueFactory((new PropertyValueFactory<Friend,String>("firstName")));
        lastName.setCellValueFactory((new PropertyValueFactory<Friend,String>("lastName")));
        date.setCellValueFactory((new PropertyValueFactory<Friend,Date>("date")));

        tableViewFriends.setItems((modelFriends));


    }
    private Collection<Friend> getFriendsList(){
        return service.getFriends(1L);


    }
    private Button createUnfriendButton(Long idFriend){
        Button addUnfriendButton=new Button();
        addUnfriendButton.setText("Unfriend");
        addUnfriendButton.setPrefWidth(30);
        addUnfriendButton.setPrefHeight(20);
        addUnfriendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    service.deleteFriendship(id,idFriend);

            }
        });
        return addUnfriendButton;
    }
    public void setService(Service service){
        this.service=service;
        modelFriends.setAll(getFriendsList());

    }
}
