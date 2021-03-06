package project.lab6.controllers.reports;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.MainViewController;
import project.lab6.service.ServiceException;
import project.lab6.service.ServiceReports;
import project.lab6.setter.SetterServiceReports;
import project.lab6.utils.Constants;
import project.lab6.utils.components.MultiDatePicker;

import java.io.File;

public class ActivityReportController extends Controller implements SetterServiceReports {


    private final Long idLoggedUser;
    private final MainViewController mainViewController;
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public VBox componentsVBox;
    @FXML
    public Button chooseFileButton;
    @FXML
    public MultiDatePicker multiDatePicker;
    private ServiceReports serviceReports;

    public ActivityReportController(Long idLoggedUser, MainViewController mainViewController) {
        this.idLoggedUser = idLoggedUser;
        this.mainViewController = mainViewController;
    }

    @Override
    public String getViewPath() {
        return Constants.View.ACTIVITY_REPORT;
    }

    @Override
    public void setServiceReports(ServiceReports serviceReports) {
        this.serviceReports = serviceReports;
    }


    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pdf", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(getStage());
        try {
            serviceReports.createFullActivityReport(selectedFile.getPath(), multiDatePicker.getStartDate(), multiDatePicker.getEndDate(), idLoggedUser);
        } catch (ServiceException serviceException) {
            AlertMessage.showErrorMessage(serviceException.getMessage());
        }


    }

    public void backToProfileAction() {
        mainViewController.openProfileView();
    }
}
