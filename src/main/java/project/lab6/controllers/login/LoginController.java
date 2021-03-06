package project.lab6.controllers.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.entities.User;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.utils.Constants;

import java.io.IOException;

public class LoginController extends Controller implements SetterServiceFriends, HasTitleBar {
    private ServiceFriends serviceFriends;
    @FXML
    private Button closeLoginButton;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    public void initialize() {
        emailTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    logInUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                try {
                    logInUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void closeLoginWindow() {
        Stage stage = (Stage) closeLoginButton.getScene().getWindow();
        stage.close();
    }

    public void logInUser() throws IOException {

        User loggedUser = serviceFriends.loginUser(emailTextField.getText(), passwordTextField.getText());
        if (loggedUser == null)
            AlertMessage.showErrorMessage("Invalid email and/or password!");
        else {
            FXMLLoader loader = Factory.getInstance().getLoader(new MainViewController(loggedUser.getId()));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(loader.load(), 600, 530);
            scene.setFill(Color.TRANSPARENT);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();
            closeLoginWindow();
        }

    }

    public void createNewAccount() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new NewAccountController());
        Stage newAccountStage = new Stage();
        newAccountStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(loader.load(), 400, 600);
        scene.setFill(Color.TRANSPARENT);
        newAccountStage.setScene(scene);
        newAccountStage.setResizable(false);
        newAccountStage.show();
        closeLoginWindow();
    }

    @Override
    public String getViewPath() {
        return Constants.View.LOGIN;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }
}
