package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterServiceFriends;
import project.lab6.setter_interface.SetterServiceMessages;

public class AddMemberController implements SetterServiceFriends, SetterServiceMessages {
    private ObservableList<UserRecord>usersRecord= FXCollections.observableArrayList();
    private ServiceFriends serviceFriends;
    private ServiceMessages serviceMessages;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<UserRecord, String> name;
    @FXML
    private TableColumn<UserRecord, Button> addButton;
    @FXML
    private TableView<UserRecord> addMembersTableView;
    @FXML
    private Button backButton;
    @FXML
    public void initialize() {
        addMembersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        name.setCellValueFactory(new PropertyValueFactory<UserRecord, String>("name"));
        addButton.setCellValueFactory(new PropertyValueFactory<UserRecord, Button>("addButton"));
        name.prefWidthProperty().bind(addMembersTableView.widthProperty().divide(2));
        addButton.prefWidthProperty().bind(addMembersTableView.widthProperty().divide(2));
     //   searchField.textProperty().addListener((obs, oldText, newText) -> findUserByName());

        addMembersTableView.setItems(usersRecord);

    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }
}
