package project.lab6.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import project.lab6.SocialNetworkApplication;
import project.lab6.controllers.Controller;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class CustomLoader extends FXMLLoader {
    private final Controller controller;

    public CustomLoader(Factory factory, Controller controller) {
        super(SocialNetworkApplication.class.getResource(controller.getViewPath()));
        setControllerFactory(controllerClass -> controller);
        this.controller = controller;
        Pattern servicePattern = Pattern.compile("Service*");
        try {
            for (var field : controller.getClass().getDeclaredFields()) {
                var type = field.getType();
                String typeName = field.getType().getTypeName();
                if (servicePattern.matcher(typeName).find()) {
                    if (type.isAssignableFrom(ServiceFriends.class)) {
                        field.setAccessible(true);
                        field.set(controller, factory.getServiceFriends());
                        System.out.println("Am setat service friends");
                    } else if (type.isAssignableFrom(ServiceMessages.class)) {
                        field.setAccessible(true);
                        field.set(controller, factory.getServiceMessages());
                        System.out.println("Am setat service messages");
                    }
//                    else if (type.isAssignableFrom(ServiceEvents.class)) {
//                        field.setAccessible(true);
//                        field.set(controller, factory.getServiceMessages());
//                    }
//                    else if (type.isAssignableFrom(ServiceReports.class)) {
//                        field.setAccessible(true);
//                        field.set(controller, factory.getServiceReports());
//                    }
                    else {
                        throw new RuntimeException("Nu a fost tratat service-ul " + typeName);
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Nu se poate initializa un field!");
        }

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
