package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.domain.User;
import project.lab6.domain.dtos.GroupChatDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceFriends;
import project.lab6.setter_interface.SetterServiceMessages;

import java.util.List;

public class CreateGroupController implements SetterServiceMessages, SetterServiceFriends, SetterIdLoggedUser {
    private GroupChatDTO group;
    private ServiceMessages serviceMessages;
    private ObservableList<UserRecord>usersRecord= FXCollections.observableArrayList();
    @FXML
    private TableColumn<UserRecord,String>name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> addParticipantsTableView;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField groupName;
    private ServiceFriends serviceFriends;
    private Long id;
    /*todo trebuie setat numele chat-ului(constructor?)
    *  trebuie adaugat userul logat in grup pe langa userii pe care doreste sa ii adauge
    * todo fiecare user adaugat in grup trebuie sters din observable */
    @FXML
    public void initialize(){
        addParticipantsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord,String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord,Button>("addButton"));
        name.prefWidthProperty().bind(addParticipantsTableView.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(addParticipantsTableView.widthProperty().divide(2));
        userNameTextField.textProperty().addListener((obs,oldText,newText)->findUserByName());
        String chatName=new String(groupName.textProperty().getValue());
        addParticipantsTableView.setItems(usersRecord);
        updateTableAtSearch("");
    }
    private Button createAddParticipantsButton(Long id){
        Button addParticipant=new Button();
        addParticipant.setText("Add");
        addParticipant.setOnAction(event->
        group.addUserInfo(new UserChatInfoDTO(serviceFriends.getUserWithFriends(id),
                        serviceFriends.getUserWithFriends(id).getFirstName()+" "
                                +serviceFriends.getUserWithFriends(id).getLastName())));
        return addParticipant;
    }
    private ObservableList<UserRecord> getUserList(String searchName){
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(id), searchName);
        ObservableList<UserRecord> userRecordObservableList=FXCollections.observableArrayList();
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addParticipantButton = createAddParticipantsButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addParticipantButton);

            userRecordObservableList.add(userRecord);
        }
        return userRecordObservableList;
    }
    private void updateTableAtSearch(String searchName){
        addParticipantsTableView.getItems().clear();
        usersRecord.setAll(getUserList(searchName));
    }
    private void findUserByName(){
        updateTableAtSearch(userNameTextField.getText());
    }
    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages=serviceMessages;
    }
    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.id = idLoggedUser;
    }

}
