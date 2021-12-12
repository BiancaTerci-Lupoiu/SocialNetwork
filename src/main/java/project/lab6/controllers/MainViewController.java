package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import project.lab6.factory.Factory;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private HBox horizontalBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setView(Constants.View.PROFILE);
    }

    private void setView(String viewName) {
        FXMLLoader loader = Factory.getInstance().getLoader(viewName);
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (horizontalBox.getChildren().size() > 1)
            horizontalBox.getChildren().set(1, node);
        else
            horizontalBox.getChildren().add(node);
    }

    public void openProfileView(ActionEvent actionEvent) {
        setView(Constants.View.PROFILE);
    }

    public void openAddFriendsView(ActionEvent actionEvent) {

    }

    public void openFriendsView(ActionEvent actionEvent) {
        setView(Constants.View.FRIENDS);
    }

    public void openRequestsView(ActionEvent actionEvent) {
        setView(Constants.View.REQUESTS);
    }
}
