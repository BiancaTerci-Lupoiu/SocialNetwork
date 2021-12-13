package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import project.lab6.controllers.AddFriendsController;
import project.lab6.controllers.LoginController;
import project.lab6.domain.User;
import project.lab6.domain.chat.Chat;
import project.lab6.factory.Factory;

import project.lab6.service.Service;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.time.LocalDateTime;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(Constants.View.LOGIN);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}