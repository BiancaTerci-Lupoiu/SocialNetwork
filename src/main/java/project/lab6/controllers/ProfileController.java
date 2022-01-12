package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import project.lab6.controllers.events.CreateEventController;
import project.lab6.controllers.events.NotificationsController;
import project.lab6.controllers.reports.ActivityReportController;
import project.lab6.controllers.reports.FriendMessagesReportController;
import project.lab6.domain.dtos.EventForUserDTO;
import project.lab6.domain.entities.User;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceFriends;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends Controller implements Initializable, SetterServiceFriends, SetterServiceEvents {
    private final Long idLoggedUser;
    private final MainViewController mainViewController;
    @FXML
    public ListView<EventForUserDTO> eventsListView;
    ObservableList<EventForUserDTO> eventsForUserDTOList = FXCollections.observableArrayList();
    private ServiceFriends serviceFriends;

    @FXML
    public Button notificationsButton;
    @FXML
    public ComboBox<String> comboBoxReports;
    @FXML
    public Button createEventButton;
    private ServiceEvents serviceEvents;
    @FXML
    Label labelHello;
    @FXML
    Label labelFirstName;
    @FXML
    Label labelLastName;
    @FXML
    Label labelEmail;

    public ProfileController(Long idLoggedUser, MainViewController mainViewController) {
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = serviceFriends.getUserWithFriends(idLoggedUser);
        labelHello.setText(String.format("Hello %s! Welcome back!", user.getFirstName()));
        labelFirstName.setText(String.format("First name: %s", user.getFirstName()));
        labelLastName.setText(String.format("Last name: %s", user.getLastName()));
        labelEmail.setText(String.format("Email: %s", user.getEmail()));
        comboBoxReports.setItems(FXCollections.observableArrayList("Activity Report", "Friend Messages Report"));
        comboBoxReports.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->openReportsView(newValue));
        eventsListView.setCellFactory(listView ->{
            ListCell<EventForUserDTO> cell =new CustomCellEvent();
            cell.setOnMouseClicked(someEvent->{
                editEvent(cell.getItem());
            });
            return cell;
        });
        eventsForUserDTOList.setAll(serviceEvents.getEventsForUser(idLoggedUser).getOwnEvents());
        eventsListView.setItems(eventsForUserDTOList);
    }

    @Override
    public String getViewPath() {
        return Constants.View.PROFILE;
    }

    public void editEvent(EventForUserDTO event){
        mainViewController.setView(new CreateEventController(idLoggedUser, mainViewController,event));
    }
    public void createEvent() {
        mainViewController.setView(new CreateEventController(idLoggedUser, mainViewController,null));
    }
    public void openReportsView(String reportType){
        if(reportType.equals("Activity Report")){
            mainViewController.setView(new ActivityReportController(idLoggedUser));
        }
        if(reportType.equals("Friend Messages Report")){
            mainViewController.setView(new FriendMessagesReportController(idLoggedUser));
        }
    }

    @Override
    public void setServiceEvents(ServiceEvents serviceEvents) {
        this.serviceEvents = serviceEvents;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    public void openNotifications() {
        mainViewController.setView(new NotificationsController(idLoggedUser));
    }

    public static class CustomCellEvent extends ListCell<EventForUserDTO> {
        HBox horizontalBox = new HBox();
        HBox dateHBox=new HBox();
        Label eventTitle = new Label();
        Label eventDate = new Label();
        EventForUserDTO event;

        public CustomCellEvent() {
            super();
            this.setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent;-fx-background-radius: 10 10 10 10;-fx-border-radius: 10 10 10 10");
            eventTitle.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 15");
            eventDate.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic");
            dateHBox.getChildren().add(eventDate);
            dateHBox.setAlignment(Pos.CENTER_RIGHT);
            dateHBox.setStyle("-fx-padding: 0 70 0 0");
            HBox.setHgrow(dateHBox, Priority.ALWAYS);
            horizontalBox.getChildren().addAll(eventTitle, dateHBox);
        }

        @Override
        protected void updateItem(EventForUserDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                event = null;
                setGraphic(null);
            } else {
                event = item;
                eventTitle.setText(event.getTitle());
                eventDate.setText(event.getDate().format(Constants.DATETIME_FORMATTER));
                setGraphic(horizontalBox);
            }
        }
    }
}
