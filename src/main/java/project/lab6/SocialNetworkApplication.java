package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.lab6.factory.Factory;
import project.lab6.utils.Constants;

import java.io.IOException;

public class SocialNetworkApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(Constants.View.LOGIN,
                InfoBuilder.create()
                        .addStage(stage)
                        .build());
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