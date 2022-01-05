package project.lab6.controllers.friends;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.controllers.Controller;
import project.lab6.controllers.utils.UserFriend;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.util.Date;
import java.util.List;

public class FriendsController extends Controller {
    ObservableList<UserFriend> modelFriends = FXCollections.observableArrayList();
    private final ServiceFriends serviceFriends;
    private final Long idLoggedUser;

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

    public FriendsController(Long idLoggedUser, ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
        this.idLoggedUser = idLoggedUser;
    }

    @FXML
    public void initialize() {
        firstName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("firstName")));
        lastName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("lastName")));
        date.setCellValueFactory((new PropertyValueFactory<UserFriend, Date>("date")));
        button.setCellValueFactory((new PropertyValueFactory<UserFriend, Button>("button1")));

        tableViewFriends.setItems(modelFriends);
        modelFriends.setAll(getFriendsList());
        tableViewFriends.getStylesheets().add(FriendsController.class.getClassLoader().getResource("project/lab6/css/tableViewNoHorizontalScroll.css").toExternalForm());

    }

    private List<UserFriend> getFriendsList() {
        return serviceFriends.getFriends(this.idLoggedUser)
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
        addUnfriendButton.setOnAction(event -> {
            serviceFriends.deleteFriendship(this.idLoggedUser, idFriend);
            modelFriends.setAll(getFriendsList());
                }
        );
        return addUnfriendButton;
    }

    @Override
    public String getViewPath() {
        return Constants.View.FRIENDS;
    }
}
