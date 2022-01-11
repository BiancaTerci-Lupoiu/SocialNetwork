package project.lab6.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import project.lab6.controllers.events.EventsController;
import project.lab6.controllers.friends.AddFriendsController;
import project.lab6.controllers.friends.FriendsController;
import project.lab6.controllers.friends.RequestsController;
import project.lab6.controllers.login.LoginController;
import project.lab6.controllers.messages.MainChatController;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends Controller implements Initializable {
    private final Long idLoggedUser;
    private final ServiceFriends serviceFriends;
    private final ServiceMessages serviceMessages;
    private final ServiceEvents serviceEvents;
    @FXML
    private HBox horizontalBox;
    private boolean messagesOpen = false;
    private Stage messagesStage = null;

    public MainViewController(Long idLoggedUser, ServiceFriends serviceFriends, ServiceMessages serviceMessages,ServiceEvents serviceEvents) {
        this.idLoggedUser = idLoggedUser;
        this.serviceFriends = serviceFriends;
        this.serviceMessages = serviceMessages;
        this.serviceEvents=serviceEvents;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setView(new ProfileController(idLoggedUser, serviceFriends,serviceEvents,this));
    }

    public void setView(Controller controller) {
        FXMLLoader loader = Factory.getInstance().getLoader(controller);
        Region region = null;
        try {
            region = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (horizontalBox.getChildren().size() > 1)
            horizontalBox.getChildren().set(1, region);
        else
            horizontalBox.getChildren().add(region);
        HBox.setHgrow(region, Priority.ALWAYS);
    }

    public void openProfileView() {
        setView(new ProfileController(idLoggedUser, serviceFriends,serviceEvents,this));
    }

    public void openAddFriendsView() {
        setView(new AddFriendsController(idLoggedUser, serviceFriends));
    }

    public void openFriendsView() {
        setView(new FriendsController(idLoggedUser, serviceFriends));
    }

    public void openRequestsView() {
        setView(new RequestsController(idLoggedUser, serviceFriends));
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(new LoginController(serviceFriends, serviceMessages,serviceEvents));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = (Stage) horizontalBox.getScene().getWindow();
        stage.setScene(scene);
    }

    public void openMessagesView() throws IOException {
        if (messagesOpen) {
            messagesStage.toFront();
            return;
        }
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(new MainChatController(idLoggedUser, serviceMessages, serviceFriends));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        messagesOpen = true;
        messagesStage = stage;
        stage.setOnCloseRequest(handle -> messagesOpen = false);
        getStage().hide();
        stage.showAndWait();
        getStage().show();
    }

    @Override
    public String getViewPath() {
        return Constants.View.MAIN_VIEW;
    }

    public void openEventsView() {
        setView(new EventsController(idLoggedUser,serviceEvents));
    }
}
