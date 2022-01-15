package project.lab6.controllers.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.dtos.Notification;
import project.lab6.service.ServiceEvents;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.utils.Constants;

public class NotificationsController extends Controller implements SetterServiceEvents {

    private final Long idLoggedUser;
    private final MainViewController mainViewController;
    @FXML
    public ListView<Notification> notificationsListView;
    @FXML
    public Button refreshButton;
    ObservableList<Notification> notificationsObservableList = FXCollections.observableArrayList();
    private ServiceEvents serviceEvents;

    public NotificationsController(Long idLoggedUser, MainViewController mainViewController) {
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
    }

    @Override
    public void setServiceEvents(ServiceEvents serviceEvents) {
        this.serviceEvents = serviceEvents;
    }

    @Override
    public String getViewPath() {
        return Constants.View.NOTIFICATIONS;
    }

    public void initialize() {
        notificationsObservableList.setAll(serviceEvents.getNotification(idLoggedUser));
        notificationsListView.setItems(notificationsObservableList);
        notificationsListView.setCellFactory(param -> new CustomCellNotification());
    }

    public void refreshNotifications() {
        notificationsObservableList.setAll(serviceEvents.getNotification(idLoggedUser));
    }

    public void backToProfileAction() {
        mainViewController.openProfileView();
    }

    public static class CustomCellNotification extends ListCell<Notification> {
        HBox mainHBox = new HBox();
        VBox messageVBox = new VBox();
        HBox dateHBox = new HBox();
        Label notificationMessage = new Label();
        Label notificationDate = new Label();
        Notification notification;

        public CustomCellNotification() {
            super();
            mainHBox.setSpacing(20);
            mainHBox.setAlignment(Pos.CENTER_LEFT);
            messageVBox.setAlignment(Pos.CENTER_LEFT);
            dateHBox.setAlignment(Pos.CENTER_RIGHT);
            messageVBox.getChildren().add(notificationMessage);
            dateHBox.getChildren().add(notificationDate);
            HBox.setHgrow(dateHBox, Priority.ALWAYS);
            mainHBox.getChildren().setAll(messageVBox, dateHBox);

            notificationMessage.setMaxWidth(256);
            notificationMessage.setWrapText(true);
            notificationMessage.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 15");
            notificationDate.setStyle("-fx-font-family: Cambria; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic");

            this.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-background-radius: 10 10 10 10;-fx-border-radius: 10 10 10 10;-fx-padding: 5 2 5 2");

        }

        @Override
        protected void updateItem(Notification item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                notification = null;
                setGraphic(null);
            } else {
                notification = item;
                notificationMessage.setText(notification.getMessage());
                notificationDate.setText(notification.getTimeOfNotifying().format(Constants.DATETIME_FORMATTER));
                setGraphic(mainHBox);
            }
        }

    }

}
