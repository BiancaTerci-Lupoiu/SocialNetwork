package project.lab6.controllers.friends;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.controllers.Controller;
import project.lab6.controllers.utils.UserRecord;
import project.lab6.domain.Status;
import project.lab6.domain.User;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.time.LocalDate;
import java.util.List;

public class AddFriendsController extends Controller {

    @FXML
    private TextField userNameTextField;
    @FXML
    private TableView<UserRecord> addFriendsTableView;
    @FXML
    private TableColumn<UserRecord, String> nameColumn;
    @FXML
    private TableColumn<UserRecord, Button> addFriendColumn;

    private ObservableList<UserRecord> userRecordList = FXCollections.observableArrayList();
    private final Long idLoggedUser;
    private final ServiceFriends serviceFriends;

    public AddFriendsController(Long idLoggedUser, ServiceFriends serviceFriends) {
        this.idLoggedUser = idLoggedUser;
        this.serviceFriends = serviceFriends;
    }

    @FXML
    public void initialize() {
        addFriendsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nameColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        addFriendColumn.prefWidthProperty().bind(addFriendsTableView.widthProperty().divide(2));
        nameColumn.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addFriendColumn.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        userNameTextField.textProperty().addListener((obs, oldText, newText) -> findUserByName());
        addFriendsTableView.setItems(userRecordList);
        updateTableWithUsersAtSearch("");
    }

    private Button createAddButton(Long idUserTo) {
        Button addFriendButton = new Button();
        addFriendButton.setText("+");
        addFriendButton.setPrefWidth(30);
        addFriendButton.setPrefHeight(30);
        addFriendButton.setOnAction(event -> {
            serviceFriends.addFriendship(idLoggedUser, idUserTo, LocalDate.now(), Status.PENDING);
            findUserByName();
        });

        return addFriendButton;
    }

    private List<UserRecord> getUserRecordList(String searchName) {
        List<User> usersList = serviceFriends.searchUsersByNameNotFriendsWithLoggedUser(serviceFriends.getUserWithFriends(idLoggedUser), searchName);
        List<UserRecord> userRecordList = FXCollections.observableArrayList();
        for (User user : usersList) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addFriendButton = createAddButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addFriendButton);

            userRecordList.add(userRecord);
        }
        return userRecordList;
    }

    private void updateTableWithUsersAtSearch(String searchName) {
        userRecordList.setAll(getUserRecordList(searchName));
    }

    public void findUserByName() {
        updateTableWithUsersAtSearch(userNameTextField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.ADD_FRIENDS;
    }
}
