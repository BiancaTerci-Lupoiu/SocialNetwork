package project.lab6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.lab6.controllers.FriendsController;
import project.lab6.domain.Friendship;
import project.lab6.domain.Message;
import project.lab6.domain.User;
import project.lab6.domain.validators.FriendshipValidator;
import project.lab6.domain.validators.MessageValidator;
import project.lab6.domain.validators.UserValidator;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.Repository;
import project.lab6.repository.RepositoryUser;
import project.lab6.repository.database.FriendshipDbRepository;
import project.lab6.repository.database.MessageDbRepository;
import project.lab6.repository.database.UserDbRepository;
import project.lab6.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/friends.fxml"));

        Validator<User>userValid=new UserValidator();
        Validator<Friendship> friendhipValid=new FriendshipValidator();
        Validator<Message> messageValid=new MessageValidator();
        UserDbRepository repoUser=new UserDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","120201",userValid);
        FriendshipDbRepository repoFriendhip=new FriendshipDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","120201",friendhipValid);
        MessageDbRepository repoMessage=new MessageDbRepository("jdbc:postgresql://localhost:5432/socialnetwork","postgres","120201",messageValid);
        Service service=new Service(repoUser,repoFriendhip,repoMessage);
        BorderPane root=fxmlLoader.load();

        FriendsController ctrl=fxmlLoader.getController();
        ctrl.setService(service);
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Friends");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}