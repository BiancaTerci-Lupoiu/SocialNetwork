package project.lab6.controllers.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import project.lab6.controllers.Controller;
import project.lab6.controllers.ProfileController;
import project.lab6.domain.dtos.EventForUserDTO;
import project.lab6.service.ServiceEvents;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;

public class EventsController extends Controller {
    private final Long idLoggedUser;
    private final ServiceEvents serviceEvents;
    ObservableList<EventForUserDTO> eventsForUserDTOList = FXCollections.observableArrayList();

    @FXML
    public Label eventDetailsLabel;
    @FXML
    public ComboBox<String> comboBoxEvents;
    @FXML
    public ListView<EventForUserDTO> eventsListView;

    public EventsController(Long idLoggedUser, ServiceEvents serviceEvents) {
        this.idLoggedUser = idLoggedUser;
        this.serviceEvents = serviceEvents;
    }

    public void initialize() {
        comboBoxEvents.setItems(FXCollections.observableArrayList("Subscribed", "Discover Events"));
        comboBoxEvents.getSelectionModel().selectedItemProperty().addListener((combo) -> setEventsList(combo.toString()));
        eventDetailsLabel.setVisible(false);
        eventsListView.setItems(eventsForUserDTOList);
    }

    @Override
    public String getViewPath() {
        return Constants.View.EVENTS;
    }


    public static class CustomCellEvent extends ListCell<EventForUserDTO> {
        HBox horizontalBox = new HBox();
        Label eventTitle = new Label();
        EventForUserDTO event;
        Button subscribeButton = new Button("Subscribe");
        Button unsubscribeButton = new Button("Unsubscribe");

        ServiceEvents serviceEvents;
        Long idLoggedUser;
        boolean isSubscribed;

        public CustomCellEvent(ServiceEvents serviceEvents, Long idLoggedUser, boolean isSubscribed) {
            super();
            this.serviceEvents = serviceEvents;
            this.idLoggedUser = idLoggedUser;
            this.isSubscribed = isSubscribed;
            this.setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent;-fx-background-radius: 10 10 10 10;-fx-border-radius: 10 10 10 10");
            eventTitle.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 15");
            subscribeButton.setStyle("-fx-background-color: #5c0e63; -fx-text-fill: #ffffff;-fx-font-family: Cambria Bold;-fx-font-size: 14;");
            unsubscribeButton.setStyle("-fx-background-color: #5c0e63; -fx-text-fill: #ffffff;-fx-font-family: Cambria Bold;-fx-font-size: 14;");

            subscribeButton.setOnAction(someEvent -> {
                serviceEvents.subscribe(idLoggedUser, event.getId(), LocalDateTime.now());
            });
            unsubscribeButton.setOnAction(someEvent -> {
                serviceEvents.unsubscribe(idLoggedUser, event.getId());
            });

            if (isSubscribed)
                horizontalBox.getChildren().setAll(eventTitle, subscribeButton);
            else
                horizontalBox.getChildren().setAll(eventTitle, unsubscribeButton);
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
                setGraphic(horizontalBox);
            }
        }
    }
    private void setEventsList(String status) {
        if (status.equals("Subscribed")) {
            eventsForUserDTOList.setAll(serviceEvents.getEventsForUser(idLoggedUser).getWithSubscription(true));
            eventsListView.setCellFactory(param ->{
                CustomCellEvent cell=new CustomCellEvent(serviceEvents, idLoggedUser, true);
                EventForUserDTO event=cell.getItem();
                cell.setOnMouseClicked(someEvent->{
                    String details=event.getTitle()+"\n"+"Date and time: "+event.getDate()+"\n"+event.getDescription();
                    eventDetailsLabel.setVisible(true);
                    eventDetailsLabel.setText(details);
                });
                
                return cell;
            });
        }
        if (status.equals("Discover Events")) {
            eventsForUserDTOList.setAll(serviceEvents.getEventsForUser(idLoggedUser).getWithSubscription(false));
            eventsListView.setCellFactory(param->new CustomCellEvent(serviceEvents,idLoggedUser,false));
        }
    }
}
