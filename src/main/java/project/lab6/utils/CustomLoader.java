package project.lab6.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import project.lab6.SocialNetworkApplication;
import project.lab6.controllers.Controller;

import java.io.IOException;
import java.io.InputStream;

public class CustomLoader extends FXMLLoader {
    private final Controller controller;

    public CustomLoader(Controller controller) {
        super(SocialNetworkApplication.class.getResource(controller.getViewPath()));
        setControllerFactory(controllerClass -> controller);
        this.controller = controller;
    }

    @Override
    public <T> T load() throws IOException {
        T result = super.load();
        controller.setRoot((Parent) result);
        return result;
    }

    @Override
    public <T> T load(InputStream inputStream) throws IOException {
        T result = super.load(inputStream);
        controller.setRoot((Parent) result);
        return result;
    }
}
