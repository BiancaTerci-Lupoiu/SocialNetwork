package project.lab6.controllers.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.entities.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.utils.Constants;

import java.io.IOException;

public class NewAccountController extends Controller implements SetterServiceFriends, HasTitleBar {
    private ServiceFriends serviceFriends;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private void closeWindow() {
        Stage stage = (Stage) emailTextField.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates a new account for the user and logs it in
     *
     * @throws IOException
     */
    public void registerUser() throws IOException {
        try {
            boolean result = serviceFriends.addUser(emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), passwordTextField.getText());
            if (result) {
                User loggedUser = serviceFriends.findUserByEmail(emailTextField.getText());
                FXMLLoader loader = Factory.getInstance().getLoader(new MainViewController(loggedUser.getId()));
                Stage mainStage = new Stage();
                mainStage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(loader.load(), 600, 530);
                scene.setFill(Color.TRANSPARENT);
                mainStage.setScene(scene);
                mainStage.setResizable(false);
                mainStage.show();
                closeWindow();
            } else {
                AlertMessage.showErrorMessage("You already have an account with this email address!");
            }
        } catch (ValidationException validationException) {
            AlertMessage.showErrorMessage(validationException.getMessage());
        }
    }

    public void backToLogIn() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new LoginController());
        Stage loginStage = new Stage();
        loginStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(loader.load(), 600, 430);
        scene.setFill(Color.TRANSPARENT);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        closeWindow();
        loginStage.show();
    }

    @Override
    public String getViewPath() {
        return Constants.View.CREATE_NEW_ACCOUNT;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }
}
