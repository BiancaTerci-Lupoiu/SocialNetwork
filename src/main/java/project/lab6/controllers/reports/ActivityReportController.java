package project.lab6.controllers.reports;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import project.lab6.controllers.Controller;
import project.lab6.service.ServiceReports;
import project.lab6.setter.SetterServiceReports;
import project.lab6.utils.Constants;
import project.lab6.utils.components.MultiDatePicker;

import java.io.File;

public class ActivityReportController extends Controller implements SetterServiceReports {


    private ServiceReports serviceReports;
    private final Long idLoggedUser;

    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public VBox componentsVBox;
    @FXML
    public Button chooseFileButton;
    @FXML
    public MultiDatePicker multiDatePicker;

    public ActivityReportController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    @Override
    public String getViewPath() {
        return Constants.View.ACTIVITY_REPORT;
    }

    @Override
    public void setServiceReports(ServiceReports serviceReports) {
        this.serviceReports = serviceReports;
    }


    public void initialize() {

    }

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pdf", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(getStage());
        if (selectedFile != null) {
            //TODO: sa apelez functia din service
        }

    }
}
