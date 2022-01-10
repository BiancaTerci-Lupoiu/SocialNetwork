package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import project.lab6.controllers.events.CreateEventController;
import project.lab6.domain.entities.User;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    private final Long idLoggedUser;
    private final ServiceFriends serviceFriends;
    private final ServiceEvents serviceEvents;
    private final MainViewController mainViewController;

    @FXML
    public Button notificationsButton;
    @FXML
    public ComboBox<String> comboBoxReports;
    @FXML
    public Button createEventButton;
    @FXML
    Label labelHello;
    @FXML
    Label labelFirstName;
    @FXML
    Label labelLastName;
    @FXML
    Label labelEmail;

    public ProfileController(Long idLoggeduser, ServiceFriends serviceFriends,ServiceEvents serviceEvents,MainViewController mainViewController) {
        this.idLoggedUser = idLoggeduser;
        this.serviceFriends = serviceFriends;
        this.serviceEvents=serviceEvents;
        this.mainViewController=mainViewController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = serviceFriends.getUserWithFriends(idLoggedUser);
        labelHello.setText(String.format("Hello %s! Welcome back!", user.getFirstName()));
        labelFirstName.setText(String.format("First name: %s", user.getFirstName()));
        labelLastName.setText(String.format("Last name: %s", user.getLastName()));
        labelEmail.setText(String.format("Email: %s", user.getEmail()));
        comboBoxReports.setItems(FXCollections.observableArrayList("Full Report","Friend Messages Report"));
    }

    @Override
    public String getViewPath() {
        return Constants.View.PROFILE;
    }

    public void createEvent() throws IOException {
        mainViewController.setView(new CreateEventController(serviceEvents,idLoggedUser,mainViewController));
    }
}
