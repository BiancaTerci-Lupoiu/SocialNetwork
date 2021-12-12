package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

public class NewAccountController implements SetterService {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private Service service;



    private void closeWindow(){
        Stage stage= (Stage) emailTextField.getScene().getWindow();
        stage.close();
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    /**
     * Creates a new account for the user and logs it in
     * @param actionEvent
     * @throws IOException
     */
    public void registerUser(ActionEvent actionEvent) throws IOException {
        //verifica daca returneaza false- exista deja useru + exceptiile de validare
        boolean result=service.addUser(emailTextField.getText(),firstNameTextField.getText(),lastNameTextField.getText(),passwordTextField.getText());
        if(result==true)
        {
            User loggedUser=service.findUserByEmail(emailTextField.getText());
            Factory.getInstance().setIdLoggedUser(loggedUser.getId());
            FXMLLoader loader=Factory.getInstance().getLoader(Constants.View.MAIN_VIEW);
            Stage mainStage=new Stage();
            Scene scene=new Scene(loader.load(),600,500);
            mainStage.setScene(scene);
            mainStage.initStyle(StageStyle.TRANSPARENT);
            mainStage.show();
            closeWindow();
        }
    }

    public void backToLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= Factory.getInstance().getLoader(Constants.View.LOGIN);
        Stage loginStage=new Stage();

        Scene scene = new Scene(loader.load(), 600, 400);
        loginStage.setScene(scene);
        loginStage.initStyle(StageStyle.TRANSPARENT);
        closeWindow();
        loginStage.show();
    }
}
