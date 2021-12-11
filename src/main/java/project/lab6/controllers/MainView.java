package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import project.lab6.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {
    @FXML
    private HBox horizontalBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
//        Node node = null;
//        try {
//            node = loader.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        horizontalBox.getChildren().add(node);
    }

    public void openProfileView(ActionEvent actionEvent) {

    }

    public void openAddUserView(ActionEvent actionEvent) {
    }

    public void openFriendsView(ActionEvent actionEvent) {
    }

    public void openRequestsView(ActionEvent actionEvent) {
    }
}
