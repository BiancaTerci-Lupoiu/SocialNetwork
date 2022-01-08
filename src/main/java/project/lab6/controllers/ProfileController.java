package project.lab6.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import project.lab6.domain.entities.User;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    private final Long idLoggedUser;
    private final ServiceFriends serviceFriends;
    @FXML
    Label labelHello;
    @FXML
    Label labelFirstName;
    @FXML
    Label labelLastName;
    @FXML
    Label labelEmail;

    public ProfileController(Long idLoggeduser, ServiceFriends serviceFriends) {
        this.idLoggedUser = idLoggeduser;
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = serviceFriends.getUserWithFriends(idLoggedUser);
        labelHello.setText(String.format("Hello %s! Welcome back!", user.getFirstName()));
        labelFirstName.setText(String.format("First name: %s", user.getFirstName()));
        labelLastName.setText(String.format("Last name: %s", user.getLastName()));
        labelEmail.setText(String.format("Email: %s", user.getEmail()));
    }

    @Override
    public String getViewPath() {
        return Constants.View.PROFILE;
    }
}
