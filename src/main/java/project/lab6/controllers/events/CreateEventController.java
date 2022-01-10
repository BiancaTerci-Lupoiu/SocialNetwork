package project.lab6.controllers.events;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.domain.validators.ValidationException;
import project.lab6.service.ServiceEvents;
import project.lab6.service.ServiceFriends;
import project.lab6.utils.Constants;

import java.time.LocalDateTime;

public class CreateEventController extends Controller {

    private final ServiceEvents serviceEvents;
    private final Long idLoggedUser;
    private final MainViewController mainViewController;


    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public TextField titleTextField;
    @FXML
    public Button createEventButton;

    public CreateEventController(ServiceEvents serviceEvents, Long idLoggedUser, MainViewController mainViewController) {
        this.serviceEvents = serviceEvents;
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
    }

    @Override
    public String getViewPath() {
        return Constants.View.CREATE_EVENT;
    }

    public void createEvent() {
        try {
            serviceEvents.createEvent(idLoggedUser, titleTextField.getText(), descriptionTextArea.getText(), LocalDateTime.now());
            mainViewController.openProfileView();
        }catch(ValidationException validationException)
        {
            AlertMessage.showErrorMessage(validationException.getMessage());
        }
        }
}
