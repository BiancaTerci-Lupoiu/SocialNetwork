package project.lab6.controllers.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.entities.User;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.io.IOException;

public class LoginController extends Controller {
    private final ServiceFriends serviceFriends;
    private final ServiceMessages serviceMessages;
    private final ServiceEvents serviceEvents;
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

    public LoginController(ServiceFriends serviceFriends, ServiceMessages serviceMessages,ServiceEvents serviceEvents) {
        this.serviceFriends = serviceFriends;
        this.serviceMessages = serviceMessages;
        this.serviceEvents=serviceEvents;
    }

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
            FXMLLoader loader = Factory.getInstance().getLoader(new MainViewController(loggedUser.getId(), serviceFriends, serviceMessages,serviceEvents));
            Stage mainStage = new Stage();
            Scene scene = new Scene(loader.load(), 600, 500);
            mainStage.setScene(scene);
            mainStage.setResizable(false);
            mainStage.show();
            closeLoginWindow();
        }

    }

    public void createNewAccount() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new NewAccountController(serviceFriends, serviceMessages,serviceEvents));
        Stage newAccountStage = new Stage();
        Scene scene = new Scene(loader.load(), 400, 570);
        newAccountStage.setScene(scene);
        newAccountStage.setResizable(false);
        newAccountStage.show();
        closeLoginWindow();
    }

    @Override
    public String getViewPath() {
        return Constants.View.LOGIN;
    }
}
