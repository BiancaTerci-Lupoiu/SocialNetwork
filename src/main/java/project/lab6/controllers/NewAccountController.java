package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.lab6.domain.User;
import project.lab6.domain.validators.ValidationException;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.setter_interface.SetterServiceFriends;
import project.lab6.utils.Constants;

import java.io.IOException;

public class NewAccountController implements SetterServiceFriends {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private ServiceFriends serviceFriends;


    private void closeWindow() {
        Stage stage = (Stage) emailTextField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    /**
     * Creates a new account for the user and logs it in
     *
     * @param actionEvent
     * @throws IOException
     */
    public void registerUser(ActionEvent actionEvent) throws IOException {
        try {
            boolean result = serviceFriends.addUser(emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), passwordTextField.getText());
            if (result) {
                User loggedUser = serviceFriends.findUserByEmail(emailTextField.getText());
                Factory.getInstance().setIdLoggedUser(loggedUser.getId());
                FXMLLoader loader = Factory.getInstance().getLoader(this, Constants.View.MAIN_VIEW);
                Stage mainStage = new Stage();
                Scene scene = new Scene(loader.load(), 600, 500);
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

    public void backToLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(this, Constants.View.LOGIN);
        Stage loginStage = new Stage();

        Scene scene = new Scene(loader.load(), 600, 400);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        closeWindow();
        loginStage.show();
    }
}
