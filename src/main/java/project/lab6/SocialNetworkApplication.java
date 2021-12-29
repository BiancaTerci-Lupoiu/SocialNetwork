package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.lab6.controllers.CreateGroupController;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.io.IOException;

public class SocialNetworkApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(Constants.View.CREATE_NEW_GROUP);
        Factory.getInstance().setIdLoggedUser(1L);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        CreateGroupController ctrl=fxmlLoader.getController();
        ServiceFriends friends=Factory.getInstance().getServiceFriends();
        ServiceMessages messages=Factory.getInstance().getServiceMessages();
        ctrl.setServiceMessages(messages);
        ctrl.setServiceFriends(friends);
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