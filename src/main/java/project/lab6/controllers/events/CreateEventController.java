package project.lab6.controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.dtos.EventForUserDTO;
import project.lab6.domain.validators.ValidationException;
import project.lab6.service.ServiceEvents;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateEventController extends Controller implements SetterServiceEvents {
    private final Long idLoggedUser;
    private final MainViewController mainViewController;
    private ServiceEvents serviceEvents;
    private final EventForUserDTO event;

    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public TextField titleTextField;
    @FXML
    public Button createEventButton;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Spinner<Integer> minutesSpinner;
    @FXML
    public Spinner<Integer> hoursSpinner;
    @FXML
    public Button backToProfileButton;


    public CreateEventController(Long idLoggedUser, MainViewController mainViewController, EventForUserDTO event) {
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
        this.event = event;
    }

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactoryHours =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        SpinnerValueFactory<Integer> valueFactoryMinutes =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minutesSpinner.setValueFactory(valueFactoryMinutes);
        hoursSpinner.setValueFactory(valueFactoryHours);
        if (event != null) {
            titleTextField.setText(event.getTitle());
            descriptionTextArea.setText(event.getDescription());
            datePicker.setValue(event.getDate().toLocalDate());
            minutesSpinner.getValueFactory().setValue(event.getDate().getMinute());
            hoursSpinner.getValueFactory().setValue(event.getDate().getHour());
            createEventButton.setText("Edit");
            createEventButton.setOnAction(someEvent -> {
                editEvent();
            });

        }
    }

    @Override
    public String getViewPath() {
        return Constants.View.CREATE_EVENT;
    }

    public void createEvent() {
        try {
            LocalTime time = LocalTime.of(hoursSpinner.getValue(), minutesSpinner.getValue());
            LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), time);
            serviceEvents.createEvent(idLoggedUser, titleTextField.getText(), descriptionTextArea.getText(), dateTime);
            mainViewController.openProfileView();
        } catch (ValidationException validationException) {
            AlertMessage.showErrorMessage(validationException.getMessage());
        }
    }

    public void editEvent() {
        try {
            LocalTime time = LocalTime.of(hoursSpinner.getValue(), minutesSpinner.getValue());
            LocalDateTime dateTime = LocalDateTime.of(datePicker.getValue(), time);
            serviceEvents.modifyEvent(event.getId(), titleTextField.getText(), descriptionTextArea.getText(), dateTime);
            mainViewController.openProfileView();
        } catch (ValidationException validationException) {
            AlertMessage.showErrorMessage(validationException.getMessage());
        }
    }

    public void backToProfileAction() {
        mainViewController.openProfileView();
    }

    @Override
    public void setServiceEvents(ServiceEvents serviceEvents) {
        this.serviceEvents = serviceEvents;
    }
}
