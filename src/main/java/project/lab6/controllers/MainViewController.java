package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
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

    public void openAddFriendsView(ActionEvent actionEvent) {
    }

    public void openFriendsView(ActionEvent actionEvent) {
    }

    public void openRequestsView(ActionEvent actionEvent) {
    }
}
