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
import project.lab6.domain.User;
import project.lab6.service.Service;

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

    private Service service;
    private Stage loginStage;

    public void setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void closeLoginWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
    }

    public void logInUser(ActionEvent actionEvent) {
        //daca result=null->mesaj email/password incorrect, daca nu result=useru conectat
        User result=service.loginUser(emailTextField.toString(),passwordTextField.toString());

    }

    public void createNewAccount(ActionEvent actionEvent) throws IOException {
        //cu factory
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("views/createNewAccount.fxml"));
        Stage newAccountStage=new Stage();
        Scene scene=new Scene(loader.load(),400, 570);


        newAccountStage.setScene(scene);
        newAccountStage.initStyle(StageStyle.TRANSPARENT);

        newAccountStage.show();
        closeLoginWindow(null);
    }
}
