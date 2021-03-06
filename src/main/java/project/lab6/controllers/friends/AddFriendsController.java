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
import project.lab6.domain.entities.User;
import project.lab6.repository.paging.PagedItems;
import project.lab6.service.ServiceFriends;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.utils.Constants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddFriendsController extends Controller implements SetterServiceFriends {

    private final ObservableList<UserRecord> userRecordList = FXCollections.observableArrayList();
    private final Long idLoggedUser;
    private ServiceFriends serviceFriends;

    @FXML
    private TextField userNameTextField;
    @FXML
    private TableView<UserRecord> addFriendsTableView;
    @FXML
    private TableColumn<UserRecord, String> nameColumn;
    @FXML
    private TableColumn<UserRecord, Button> addFriendColumn;
    private PagedItems<User> pagedItems;

    public AddFriendsController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

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

    private List<UserRecord> getUserRecordFromUsers(List<User> users) {
        List<UserRecord> list = new ArrayList<>();
        for (User user : users) {
            String name = user.getLastName() + " " + user.getFirstName();
            Button addFriendButton = createAddButton(user.getId());
            UserRecord userRecord = new UserRecord(user.getId(), name, addFriendButton);

            list.add(userRecord);
        }
        return list;
    }

    private PagedItems<User> getPagedItemsSearch(String searchName) {
        User user = serviceFriends.getUserWithFriends(idLoggedUser);
        return serviceFriends.searchUsersByNameNotFriendsWithLoggedUser(user, searchName);
    }

    private void updateTableWithUsersAtSearch(String searchName) {
        pagedItems = getPagedItemsSearch(searchName);
        var items = pagedItems.getNextItems();
        var record = getUserRecordFromUsers(items);
        try {
            userRecordList.setAll(record);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void findUserByName() {
        updateTableWithUsersAtSearch(userNameTextField.getText());
    }

    @Override
    public String getViewPath() {
        return Constants.View.ADD_FRIENDS;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    public void previousPage() {
        var items = pagedItems.getPreviousItems();
        if (items.size() > 0)
            userRecordList.setAll(getUserRecordFromUsers(items));
    }

    public void nextPage() {
        var items = pagedItems.getNextItems();
        if (items.size() > 0)
            userRecordList.setAll(getUserRecordFromUsers(items));
    }
}
