package project.lab6.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import project.lab6.SocialNetworkApplication;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.factory.Factory;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.setter.SetterServiceReports;

import java.io.IOException;
import java.io.InputStream;

public class CustomLoader extends FXMLLoader {
    private final Controller controller;

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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T load() throws IOException {
        T result = super.load();

        controller.setRoot((Parent) result);
        if (controller instanceof HasTitleBar) {
            //TODO: adauga acel TitleBar si seteaza ca root-ul controlerului sa fie acest nou root
        }
        return (T) controller.getRoot();
    }
}
