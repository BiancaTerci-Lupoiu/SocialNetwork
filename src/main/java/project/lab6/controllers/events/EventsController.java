package project.lab6.controllers.events;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import project.lab6.controllers.Controller;
import project.lab6.domain.dtos.EventForUserDTO;
import project.lab6.repository.paging.PagedItems;
import project.lab6.service.ServiceEvents;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public class EventsController extends Controller implements SetterServiceEvents {
    private final Long idLoggedUser;
    @FXML
    public ComboBox<String> comboBoxEvents;
    @FXML
    public ListView<EventForUserDTO> eventsListView;
    ObservableList<EventForUserDTO> eventsForUserDTOList = FXCollections.observableArrayList();
    private ServiceEvents serviceEvents;
    private PagedItems<EventForUserDTO> pagedEvents;

    public EventsController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public void initialize() {
        comboBoxEvents.setItems(FXCollections.observableArrayList("Subscribed", "Discover Events", "All Events"));
        comboBoxEvents.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setEventsList(newValue)
        );
        eventsListView.setItems(eventsForUserDTOList);
        eventsListView.setCellFactory(someEvent -> new CustomCellEvent(serviceEvents, idLoggedUser, this::updateEventsList));
        eventsListView.getStylesheets().add("project/lab6/css/listViewNoHorizontalScroll.css");
        comboBoxEvents.setValue("All Events");
        setEventsList("All Events");
    }

    @Override
    public String getViewPath() {
        return Constants.View.EVENTS;
    }

    @Override
    public void setServiceEvents(ServiceEvents serviceEvents) {
        this.serviceEvents = serviceEvents;
    }

    private void setEventsList(String status) {
        if (status == null)
            status = "All Events";
        if (status.equals("Subscribed")) {
            pagedEvents = serviceEvents.getSubscribedEvents(idLoggedUser, true);
            eventsForUserDTOList.setAll(pagedEvents.getNextItems());
        }
        if (status.equals("Discover Events")) {
            pagedEvents = serviceEvents.getSubscribedEvents(idLoggedUser, false);
            eventsForUserDTOList.setAll(pagedEvents.getNextItems());
        }
        if (status.equals("All Events")) {
            pagedEvents = serviceEvents.getAllEvents(idLoggedUser);
            eventsForUserDTOList.setAll(pagedEvents.getNextItems());
        }
    }

    private void updateEventsList(boolean unused) {
        setEventsList(comboBoxEvents.getValue());
    }

    public void previousPage() {
        var previousPageItems = pagedEvents.getPreviousItems();
        if (!previousPageItems.isEmpty())
            eventsForUserDTOList.setAll(previousPageItems);
    }

    public void nextPage() {
        var nextPageItems = pagedEvents.getNextItems();
        if (!nextPageItems.isEmpty())
            eventsForUserDTOList.setAll(nextPageItems);
    }

    public static class CustomCellEvent extends ListCell<EventForUserDTO> {

        HBox titleButtonHBox = new HBox();
        HBox buttonHBox = new HBox();
        Label eventTitle = new Label();
        EventForUserDTO event;
        Button subscribeButton = new Button("Subscribe");
        Button unsubscribeButton = new Button("Unsubscribe");
        VBox mainVBox = new VBox();
        Label description = new Label();
        Label date = new Label();
        HBox eventDetailsHBox = new HBox();
        Label descriptionTag = new Label("Description:");
        Label dateTag = new Label("    Date&Time:");

        ServiceEvents serviceEvents;
        Long idLoggedUser;
        Consumer<Boolean> updateEventsList;

        public CustomCellEvent(ServiceEvents serviceEvents, Long idLoggedUser, Consumer<Boolean> updateEventsList) {
            this.serviceEvents = serviceEvents;
            this.idLoggedUser = idLoggedUser;
            this.updateEventsList = updateEventsList;
            this.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-background-radius: 10 10 10 10;-fx-border-radius: 10 10 10 10");

            titleButtonHBox.setSpacing(120);
            eventTitle.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 16");
            subscribeButton.setStyle("-fx-background-color: #5c0e63; -fx-text-fill: #ffffff;-fx-font-family: Cambria Bold;-fx-font-size: 14;");
            unsubscribeButton.setStyle("-fx-background-color: #5c0e63; -fx-text-fill: #ffffff;-fx-font-family: Cambria Bold;-fx-font-size: 14;");

            subscribeButton.setOnAction(someEvent -> {
                serviceEvents.subscribe(idLoggedUser, event.getId(), LocalDateTime.now());
                updateEventsList.accept(true);
            });
            unsubscribeButton.setOnAction(someEvent -> {
                serviceEvents.unsubscribe(idLoggedUser, event.getId());
                updateEventsList.accept(true);
            });

            buttonHBox.setAlignment(Pos.CENTER_RIGHT);
            buttonHBox.setStyle("-fx-padding: 0 40 0 0");
            HBox.setHgrow(buttonHBox, Priority.ALWAYS);

            eventDetailsHBox.getChildren().addAll(descriptionTag, description, dateTag, date);
            eventDetailsHBox.setSpacing(5);
            eventDetailsHBox.setStyle("-fx-padding: 5 0 0 0");
            description.setStyle("-fx-font-family: Cambria; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 14");
            date.setStyle("-fx-font-family: Cambria; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic");
            description.setMaxWidth(110);
            description.setWrapText(true);
            descriptionTag.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic");
            dateTag.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63;-fx-background-color: transparent; -fx-font-size: 14;-fx-font-style: Italic");
            mainVBox.setStyle("-fx-padding: 0 0 5 0");
            mainVBox.getChildren().add(titleButtonHBox);
            this.setOnMouseClicked(someEvent -> {
                if (mainVBox.getChildren().size() == 1) {
                    mainVBox.getChildren().add(eventDetailsHBox);
                    description.setText(event.getDescription());
                    date.setText(event.getDate().format(Constants.DATETIME_FORMATTER));
                }
            });

            this.focusedProperty().addListener((observable, oldValue, newValue) ->
            {
                if (oldValue && !newValue && mainVBox.getChildren().size() > 1)
                    mainVBox.getChildren().remove(1);
            });

            titleButtonHBox.getChildren().setAll(eventTitle, buttonHBox);
        }

        @Override
        protected void updateItem(EventForUserDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                event = null;
                setGraphic(null);
            } else {
                event = item;

                if (event.isSubscribed())
                    buttonHBox.getChildren().setAll(unsubscribeButton);
                else
                    buttonHBox.getChildren().setAll(subscribeButton);

                eventTitle.setText(event.getTitle());
                setGraphic(mainVBox);

            }
        }
    }

}
