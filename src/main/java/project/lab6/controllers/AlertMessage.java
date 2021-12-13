package project.lab6.controllers;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class AlertMessage {
    //TODO: sa stilizam aceste ferestre
    public static void showInfoMessage(String text){
        Alert infoAlert=new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setContentText(text);
        infoAlert.initStyle(StageStyle.TRANSPARENT);

    }
    public static void showErrorMessage(String errorText)
    {
        Alert infoAlert=new Alert(Alert.AlertType.ERROR);
        infoAlert.setContentText(errorText);
        infoAlert.initStyle(StageStyle.TRANSPARENT);
    }
}
