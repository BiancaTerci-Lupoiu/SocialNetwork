package project.lab6.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;

public class AlertMessage {
    //TODO: sa stilizam aceste ferestre
    public static void showInfoMessage(String text) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setContentText(text);
        infoAlert.initStyle(StageStyle.DECORATED);
        infoAlert.show();
    }

    public static void showErrorMessage(String errorText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setContentText(errorText);
        errorAlert.setHeaderText("Error");
        errorAlert.initStyle(StageStyle.DECORATED);
        DialogPane dialogPane = errorAlert.getDialogPane();
        dialogPane.getStylesheets().add(AlertMessage.class.getClassLoader().getResource("project/lab6/css/errorAlert.css").toExternalForm());

        errorAlert.show();

    }
}
