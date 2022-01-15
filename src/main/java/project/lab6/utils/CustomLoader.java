package project.lab6.utils;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.lab6.SocialNetworkApplication;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.factory.Factory;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.setter.SetterServiceReports;

import java.io.IOException;

public class CustomLoader extends FXMLLoader {
    private final Controller controller;

    private double xOffset = 0;
    private double yOffset = 0;

    public CustomLoader(Factory factory, Controller controller) {
        super(SocialNetworkApplication.class.getResource(controller.getViewPath()));
        setControllerFactory(controllerClass -> controller);
        this.controller = controller;
        setServices(factory);
    }

    private void setServices(Factory factory) {
        if (controller instanceof SetterServiceFriends setter)
            setter.setServiceFriends(factory.getServiceFriends());
        if (controller instanceof SetterServiceMessages setter)
            setter.setServiceMessages(factory.getServiceMessages());
        if (controller instanceof SetterServiceEvents setter)
            setter.setServiceEvents(factory.getServiceEvents());
        if (controller instanceof SetterServiceReports setter)
            setter.setServiceReports(factory.getServiceReports());
    }

    private VBox getTitleBar() {
        VBox mainVBox = new VBox();
        mainVBox.setStyle("-fx-background-color: transparent");
        HBox buttonsHBox = new HBox();
        buttonsHBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsHBox.setPrefHeight(30);
        buttonsHBox.setMinHeight(30);
        buttonsHBox.setMaxHeight(30);
        buttonsHBox.setStyle("-fx-background-color: #5c0e63;-fx-background-radius: 15 15 0 0; -fx-border-width: 0 0 2 0; -fx-border-color:  white");
        Button closeButton = new Button("x");
        closeButton.setStyle("-fx-background-color: transparent;-fx-text-fill: white;-fx-font-family: Cambria Bold;-fx-font-size: 18");
        closeButton.setOnAction(event -> {
            controller.getStage().close();
        });
        buttonsHBox.getChildren().setAll(closeButton);
        mainVBox.getChildren().add(buttonsHBox);

        buttonsHBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        buttonsHBox.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = controller.getStage();
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        return mainVBox;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T load() throws IOException {
        T result = super.load();

        controller.setRoot((Parent) result);
        if (controller instanceof HasTitleBar) {
            VBox vBox = getTitleBar();
            vBox.getChildren().add(controller.getRoot());
            controller.setRoot(vBox);
        }
        return (T) controller.getRoot();
    }
}
