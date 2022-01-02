package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.util.List;

public class OpenPrivateChatController extends Controller {
    private ObservableList<UserRecord> usersRecord = FXCollections.observableArrayList();
    private final ServiceFriends serviceFriends;
    private final ServiceMessages serviceMessages;
    private final Long idLoggedUser;

    public OpenPrivateChatController(ServiceFriends serviceFriends, ServiceMessages serviceMessages, Long idLoggedUser) {
        this.serviceFriends = serviceFriends;
        this.serviceMessages = serviceMessages;
        this.idLoggedUser = idLoggedUser;
    }

    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<UserRecord, String> name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> usersTable;

    @FXML
    public void initialize() {
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        name.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(usersTable.widthProperty().divide(2));
        searchField.textProperty().addListener((obs, oldText, newText) -> findUserByName());
        usersTable.setItems(usersRecord);
        updateTableAtSearch("");
    }

    private Button createOpenChatButton(Long id) {
        Button addParticipantButton = new Button();
        addParticipantButton.setText("Open Chat");
        addParticipantButton.setOnAction(event ->
        {   Chat chat=serviceMessages.getOrCreatePrivateChatBetweenUsers(idLoggedUser,id);
            //todo
            getStage().close();
        });
        return addParticipantButton;
    }

    private ObservableList<UserRecord> getUserList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByName(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        ObservableList<UserRecord> userRecordObservableList = FXCollections.observableArrayList();
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addParticipantButton = createOpenChatButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addParticipantButton);

            userRecordObservableList.add(userRecord);
        }
        return userRecordObservableList;
    }

    private void updateTableAtSearch(String searchName) {
        usersTable.getItems().clear();
        usersRecord.setAll(getUserList(searchName));
    }

    private void findUserByName() {
        updateTableAtSearch(searchField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.OPEN_PRIVATE_CHAT;
    }
}
