package project.lab6.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import project.lab6.domain.User;
import project.lab6.service.Service;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterService;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable, SetterService, SetterIdLoggedUser {
    @FXML
    Label labelHello;

    @FXML
    Label labelFirstName;

    @FXML
    Label labelLastName;

    @FXML
    Label labelEmail;

    Long idLoggeduser;
    Service service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = service.getUserWithFriends(idLoggeduser);
        labelHello.setText(String.format("Hello %s! Welcome back!",user.getFirstName()));
        labelFirstName.setText(String.format("First name: %s",user.getFirstName()));
        labelLastName.setText(String.format("Last name: %s",user.getLastName()));
        labelEmail.setText(String.format("Email: %s",user.getEmail()));
    }

    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.idLoggeduser = idLoggedUser;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }
}