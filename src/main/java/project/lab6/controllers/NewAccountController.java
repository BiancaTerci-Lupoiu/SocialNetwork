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
import project.lab6.service.Service;

import java.io.IOException;

public class NewAccountController {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private Service service;

    private Stage newAccountStage;

    private void closeWindow(){
        //newAccountStage.close();
        Stage stage= (Stage) emailTextField.getScene().getWindow();
        stage.close();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setNewAccountStage(Stage newAccountStage) {
        this.newAccountStage = newAccountStage;
    }

    public void registerUser(ActionEvent actionEvent) {
        //verifica daca returneaza false- exista deja useru + exceptiile de validare
        service.addUser(emailTextField.toString(),firstNameTextField.toString(),lastNameTextField.toString(),passwordTextField.toString());
    }

    public void backToLogIn(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
        Stage loginStage=new Stage();

        Scene scene = new Scene(loader.load(), 600, 400);
        loginStage.setScene(scene);
        loginStage.initStyle(StageStyle.UNDECORATED);
        closeWindow();
        loginStage.show();
    }
}
