package project.lab6.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import project.lab6.domain.User;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable {
    @FXML
    Label labelHello;
    @FXML
    Label labelFirstName;
    @FXML
    Label labelLastName;
    @FXML
    Label labelEmail;

    private final Long idLoggeduser;
    private final ServiceFriends serviceFriends;

    public ProfileController(Long idLoggeduser, ServiceFriends serviceFriends) {
        this.idLoggeduser = idLoggeduser;
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = serviceFriends.getUserWithFriends(idLoggeduser);
        labelHello.setText(String.format("Hello %s! Welcome back!", user.getLastName()));
        labelFirstName.setText(String.format("First name: %s", user.getFirstName()));
        labelLastName.setText(String.format("Last name: %s", user.getLastName()));
        labelEmail.setText(String.format("Email: %s", user.getEmail()));
    }

    @Override
    public String getViewPath() {
        return Constants.View.PROFILE;
    }
}
