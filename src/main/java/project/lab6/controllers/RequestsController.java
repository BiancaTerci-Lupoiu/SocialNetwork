package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.domain.DirectedStatus;
import project.lab6.domain.Status;
import project.lab6.service.Service;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RequestsController implements SetterService, SetterIdLoggedUser {
    private Long id;
    private Service service;
    ObservableList<UserFriend> modelFriends = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> comboBoxStatus;
    @FXML
    TableColumn<UserFriend, String> firstName;
    @FXML
    TableColumn<UserFriend, String> lastName;
    @FXML
    TableColumn<UserFriend, Date> date;
    @FXML
    TableColumn<UserFriend, Button> buttonAccept;
    @FXML
    TableColumn<UserFriend, Button> buttonCancel;
    @FXML
    TableColumn<UserFriend,Button>buttonDeny;
    @FXML
    TableView<UserFriend> tableViewRequests;


    @FXML
    public void initialize() {
        comboBoxStatus.setPromptText("Select status");
        comboBoxStatus.setItems(FXCollections.observableArrayList("Sent", "Received"));

        comboBoxStatus.getSelectionModel().selectedItemProperty().addListener(
                (x)->initializeCombo(x.toString())
        );
        firstName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("firstName")));
        lastName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("lastName")));
        date.setCellValueFactory((new PropertyValueFactory<UserFriend, Date>("date")));
        /*if (Objects.equals(selected, "Sent")) {
            buttonCancel.setCellValueFactory(new PropertyValueFactory<UserFriend, Button>("button1"));
            modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_SEND));
        }
        if(Objects.equals(selected, "Received")){
            buttonCancel.setCellValueFactory(new PropertyValueFactory<>("button1"));
            buttonAccept.setCellValueFactory(new PropertyValueFactory<>("button2"));
            modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));
        }
        tableViewRequests.setItems(modelFriends);*/
        comboBoxStatus.getSelectionModel().selectedItemProperty().addListener(
                (x,y,z)->initializeCombo(z.toString())
        );
    }
    @FXML
    public void initializeCombo(String status){
        if (Objects.equals(status, "Sent")) {
            buttonCancel.setCellValueFactory(new PropertyValueFactory<UserFriend, Button>("button1"));
            modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_SEND));

        }
        if(Objects.equals(status, "Received")){
            buttonCancel.setCellValueFactory(new PropertyValueFactory<>("button1"));
            buttonAccept.setCellValueFactory(new PropertyValueFactory<>("button2"));
            modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));

        }
        tableViewRequests.setItems(modelFriends);
    }

    public List<UserFriend> getFriendsList(DirectedStatus status) {
        if (status == DirectedStatus.PENDING_SEND)
            return service.getUserWithFriends(this.id)
                    .getFriends(status)
                    .stream()
                    .map(n -> new UserFriend(n.getUser().getId(),
                            n.getUser().getFirstName(),
                            n.getUser().getLastName(),
                            n.getDate(),
                            createCancelButton(n.getUser().getId()))).toList();
        else
            return service.getUserWithFriends(this.id)
                    .getFriends(status)
                    .stream()
                    .map(n -> new UserFriend(n.getUser().getId(),
                            n.getUser().getFirstName(),
                            n.getUser().getLastName(),
                            n.getDate(),
                            createDenyButton(n.getUser().getId()),createAcceptButton(n.getUser().getId()))).toList();
    }

    private Button createDenyButton(Long idFriend) {
        Button addDenyButton = new Button();
        addDenyButton.setText("Deny");
        /*addUnfriendButton.setPrefWidth(70);
        addUnfriendButton.setPrefHeight(30);*/
        addDenyButton.setOnAction(event -> {
                    service.modifyFriendRequestStatus(idFriend,this.id, Status.REJECTED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));
                }
        );
        return addDenyButton;
    }
    private Button createCancelButton(Long idFriend){
        Button addCancelButton=new Button();
        addCancelButton.setText("Cancel");
        addCancelButton.setOnAction(event -> {
                    service.modifyFriendRequestStatus(idFriend,this.id, Status.REJECTED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_SEND));
                }
        );
        return addCancelButton;
    }
    private Button createAcceptButton(Long idFriend){
        Button addAcceptButton = new Button();
        addAcceptButton.setText("Accept");
        addAcceptButton.setOnAction(event -> {
                    service.modifyFriendRequestStatus(idFriend,this.id, Status.APPROVED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));
                }
        );
        return addAcceptButton;
    }

    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.id = idLoggedUser;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }
}
