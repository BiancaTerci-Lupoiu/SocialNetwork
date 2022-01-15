package project.lab6.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.events.EventsController;
import project.lab6.controllers.friends.AddFriendsController;
import project.lab6.controllers.friends.FriendsController;
import project.lab6.controllers.friends.RequestsController;
import project.lab6.controllers.login.LoginController;
import project.lab6.controllers.messages.MainChatController;
import project.lab6.factory.Factory;
import project.lab6.utils.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController extends Controller implements Initializable, HasTitleBar {
    private final Long idLoggedUser;

    @FXML
    private HBox horizontalBox;

    public MainViewController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openProfileView();
    }

    public void setView(Controller controller) {
        FXMLLoader loader = Factory.getInstance().getLoader(controller);
        Region region = null;
        try {
            region = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (region == null)
            return;
        if (horizontalBox.getChildren().size() > 1)
            horizontalBox.getChildren().set(1, region);
        else
            horizontalBox.getChildren().add(region);
        //region.setMaxSize(Double.MIN_VALUE,Double.MIN_VALUE);
        //region.setMinSize(Double.MIN_VALUE,Double.MIN_VALUE);
        region.setPrefHeight(500);
        region.setPrefWidth(500);
        HBox.setHgrow(region, Priority.ALWAYS);
    }

    public void openProfileView() {
        setView(new ProfileController(idLoggedUser, this));
    }

    public void openAddFriendsView() {
        setView(new AddFriendsController(idLoggedUser));
    }

    public void openFriendsView() {
        setView(new FriendsController(idLoggedUser));
    }

    public void openRequestsView() {
        setView(new RequestsController(idLoggedUser));
    }

    public void logout() throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(new LoginController());
        Scene scene = new Scene(fxmlLoader.load(), 600, 430);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage) horizontalBox.getScene().getWindow();
        stage.setScene(scene);
    }

    public void openMessagesView() throws IOException {
        FXMLLoader fxmlLoader = Factory.getInstance().getLoader(new MainChatController(idLoggedUser));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 530);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        getStage().hide();
        stage.showAndWait();
        getStage().show();
    }

    @Override
    public String getViewPath() {
        return Constants.View.MAIN_VIEW;
    }

    public void openEventsView() {
        setView(new EventsController(idLoggedUser));
    }
}
