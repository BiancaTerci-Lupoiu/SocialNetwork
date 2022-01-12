package project.lab6.controllers.reports;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.lab6.controllers.Controller;
import project.lab6.service.ServiceReports;
import project.lab6.setter.SetterServiceReports;
import project.lab6.utils.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class ActivityReportController extends Controller implements Initializable, SetterServiceReports {
    private ServiceReports serviceReports;
    private final Long idLoggedUser;

    @FXML
    public AnchorPane mainAnchorPane;

    public ActivityReportController(Long idLoggedUser){
        this.idLoggedUser=idLoggedUser;
    }
    @Override
    public String getViewPath() {
        return Constants.View.ACTIVITY_REPORT;
    }

    @Override
    public void setServiceReports(ServiceReports serviceReports) {
        this.serviceReports=serviceReports;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Menu file = new Menu("File");
        MenuItem item = new MenuItem("Save");
        file.getItems().addAll(item);
        //Creating a File chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        //Adding action on the menu item
        item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //Opening a dialog box
                fileChooser.showSaveDialog(getStage());
            }
        });
        //Creating a menu bar and adding menu to it.
        MenuBar menuBar = new MenuBar(file);
        Group root = new Group(menuBar);
        Stage stage=new Stage();
        Scene scene = new Scene(root, 595, 355, Color.BEIGE);
        stage.setTitle("File Chooser Example");
        stage.setScene(scene);
        stage.show();
    }
}
