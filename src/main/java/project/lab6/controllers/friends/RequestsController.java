package project.lab6.controllers.friends;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.lab6.controllers.Controller;
import project.lab6.controllers.utils.UserFriend;
import project.lab6.domain.DirectedStatus;
import project.lab6.domain.Status;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RequestsController extends Controller {
    private final Long idLoggedUser;
    private final ServiceFriends serviceFriends;
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
    TableColumn<UserFriend, Button> buttonDeny;
    @FXML
    TableView<UserFriend> tableViewRequests;

    public RequestsController(Long idLoggedUser, ServiceFriends serviceFriends) {
        this.idLoggedUser = idLoggedUser;
        this.serviceFriends = serviceFriends;
    }

    @FXML
    public void initialize() {
        comboBoxStatus.setPromptText("Select status");
        comboBoxStatus.setItems(FXCollections.observableArrayList("Sent", "Received"));

        comboBoxStatus.getSelectionModel().selectedItemProperty().addListener((x) -> initializeCombo(x.toString()));
        firstName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("firstName")));
        lastName.setCellValueFactory((new PropertyValueFactory<UserFriend, String>("lastName")));
        date.setCellValueFactory((new PropertyValueFactory<UserFriend, Date>("date")));

        comboBoxStatus.getSelectionModel().selectedItemProperty().addListener(
                (x,y,z)->initializeCombo(z.toString())
        );
        tableViewRequests.getStylesheets().add(RequestsController.class.getClassLoader().getResource("project/lab6/css/tableViewNoHorizontalScroll.css").toExternalForm());

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
            return serviceFriends.getUserWithFriends(this.idLoggedUser)
                    .getFriends(status)
                    .stream()
                    .map(n -> new UserFriend(n.getUser().getId(),
                            n.getUser().getFirstName(),
                            n.getUser().getLastName(),
                            n.getDate(),
                            createCancelButton(n.getUser().getId()))).toList();
        else
            return serviceFriends.getUserWithFriends(this.idLoggedUser)
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
        addDenyButton.setOnAction(event -> {
                    serviceFriends.modifyFriendRequestStatus(idFriend,this.idLoggedUser, Status.REJECTED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));
                }
        );
        return addDenyButton;
    }
    private Button createCancelButton(Long idFriend){
        Button addCancelButton=new Button();
        addCancelButton.setText("Cancel");
        addCancelButton.setOnAction(event -> {
                    serviceFriends.modifyFriendRequestStatus(idFriend,this.idLoggedUser, Status.REJECTED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_SEND));
                }
        );
        return addCancelButton;
    }
    private Button createAcceptButton(Long idFriend){
        Button addAcceptButton = new Button();
        addAcceptButton.setText("Accept");
        addAcceptButton.setOnAction(event -> {
                    serviceFriends.modifyFriendRequestStatus(idFriend,this.idLoggedUser, Status.APPROVED);
                    modelFriends.setAll(getFriendsList(DirectedStatus.PENDING_RECEIVED));
                }
        );
        return addAcceptButton;
    }

    @Override
    public String getViewPath() {
        return Constants.View.REQUESTS;
    }
}
