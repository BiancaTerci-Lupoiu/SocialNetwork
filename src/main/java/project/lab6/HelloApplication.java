package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.LoginController;
import project.lab6.factory.Factory;
import project.lab6.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/addFriends.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        //LoginController loginController=fxmlLoader.getController();
        //loginController.setService(Factory.getInstance().getService());
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}