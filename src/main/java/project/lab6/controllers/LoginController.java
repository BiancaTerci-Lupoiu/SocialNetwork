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
import project.lab6.factory.Factory;
import project.lab6.service.Service;
import project.lab6.setter_interface.SetterService;
import project.lab6.utils.Constants;

import java.io.IOException;

public class LoginController implements SetterService {
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

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    public void closeLoginWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
    }

    public void logInUser(ActionEvent actionEvent) throws IOException {
        //daca result=null->mesaj email/password incorrect, daca nu result=useru conectat
        User loggedUser = service.loginUser(emailTextField.getText(), passwordTextField.getText());
        if (loggedUser != null) {
            Factory.getInstance().setIdLoggedUser(loggedUser.getId());
            FXMLLoader loader=Factory.getInstance().getLoader(Constants.View.MAIN_VIEW);
            Stage mainStage=new Stage();
            Scene scene=new Scene(loader.load(),600,500);
            mainStage.setScene(scene);
            mainStage.initStyle(StageStyle.TRANSPARENT);
            mainStage.show();
            closeLoginWindow(null);
        }

    }

    public void createNewAccount(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(Constants.View.CREATE_NEW_ACCOUNT);
        Stage newAccountStage = new Stage();
        Scene scene = new Scene(loader.load(), 400, 570);
        newAccountStage.setScene(scene);
        newAccountStage.initStyle(StageStyle.TRANSPARENT);
        newAccountStage.show();
        closeLoginWindow(null);
    }
}
