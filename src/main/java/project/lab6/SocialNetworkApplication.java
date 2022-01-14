package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.login.LoginController;
import project.lab6.factory.Factory;

import java.io.IOException;

public class SocialNetworkApplication extends Application {
    public static void main(String[] args) throws Exception {
        try (Factory ignored = Factory.getInstance()) {
            launch();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Factory factory = Factory.getInstance();
        FXMLLoader fxmlLoader = factory.getLoader(new LoginController());
        Scene scene = new Scene(fxmlLoader.load(), 600, 430);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }
}