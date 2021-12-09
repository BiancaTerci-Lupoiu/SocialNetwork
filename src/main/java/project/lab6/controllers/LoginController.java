package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.HelloApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button newAccountButton;
    @FXML
    private Button closeLoginButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    public void closeLoginWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
    }

    public void logInUser(ActionEvent actionEvent) {
    }

    public void createNewAccount(ActionEvent actionEvent) throws IOException {
        /**FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/createNewAccount.fxml"));
        Stage newAccountStage=new Stage();
        Scene scene=new Scene(loader.load(),630, 400);

        newAccountStage.setScene(scene);
        newAccountStage.show();*/
    }
}
