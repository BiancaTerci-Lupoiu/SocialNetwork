package project.lab6.controllers.events;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.validators.ValidationException;
import project.lab6.service.ServiceEvents;
import project.lab6.setter.SetterServiceEvents;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateEventController extends Controller implements SetterServiceEvents {
    private final Long idLoggedUser;
    private final MainViewController mainViewController;
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
    private ServiceEvents serviceEvents;

    public CreateEventController(Long idLoggedUser, MainViewController mainViewController) {
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
    }

    public void initialize() {
        SpinnerValueFactory<Integer> valueFactoryHours =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        SpinnerValueFactory<Integer> valueFactoryMinutes =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minutesSpinner.setValueFactory(valueFactoryMinutes);
        hoursSpinner.setValueFactory(valueFactoryHours);
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

    public void backToProfileAction() {
        mainViewController.openProfileView();
    }

    @Override
    public void setServiceEvents(ServiceEvents serviceEvents) {
        this.serviceEvents = serviceEvents;
    }
}
