package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.lab6.controllers.LoginController;
import project.lab6.factory.Factory;

import java.io.IOException;

public class SocialNetworkApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Factory factory = Factory.getInstance();
        FXMLLoader fxmlLoader = factory.getLoader(new LoginController(factory.getServiceFriends(),factory.getServiceMessages()));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        try (Factory ignored = Factory.getInstance()) {
            launch();
        }
    }
}