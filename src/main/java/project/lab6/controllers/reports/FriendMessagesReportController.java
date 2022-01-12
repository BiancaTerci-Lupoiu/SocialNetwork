package project.lab6.controllers.reports;

import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import project.lab6.controllers.Controller;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceReports;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceReports;
import project.lab6.utils.Constants;

import java.io.File;

public class FriendMessagesReportController extends Controller implements SetterServiceReports, SetterServiceFriends {

    private ServiceReports serviceReports;
    private ServiceFriends serviceFriends;
    private final Long idLoggedUser;

    public FriendMessagesReportController(Long idLoggedUser){
        this.idLoggedUser=idLoggedUser;
    }
    @Override
    public String getViewPath() {
        return Constants.View.FRIEND_MESSAGES_REPORT;
    }

    @Override
    public void setServiceReports(ServiceReports serviceReports) {
        this.serviceReports=serviceReports;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends=serviceFriends;
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
