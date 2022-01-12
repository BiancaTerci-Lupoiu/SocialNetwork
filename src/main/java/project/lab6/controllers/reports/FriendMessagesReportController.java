package project.lab6.controllers.reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.domain.Friend;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.service.ServiceException;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceReports;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceReports;
import project.lab6.utils.Constants;
import project.lab6.utils.components.MultiDatePicker;

import java.io.File;

public class FriendMessagesReportController extends Controller implements SetterServiceReports, SetterServiceFriends {


    private ServiceReports serviceReports;
    private ServiceFriends serviceFriends;
    private final Long idLoggedUser;
    ObservableList<Friend> friendsObservableList = FXCollections.observableArrayList();

    @FXML
    public ListView<Friend> friendsListView;
    @FXML
    public TextField searchFriendsTextField;
    @FXML
    public Button searchFriendsButton;
    @FXML
    public MultiDatePicker multiDatePicker;

    public FriendMessagesReportController(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public void initialize() {
        friendsObservableList.setAll(serviceFriends.getFriends(idLoggedUser));
        friendsListView.setItems(friendsObservableList);
        searchFriendsTextField.textProperty().addListener((obs, oldText, newText) -> searchFriendsByName());
        friendsListView.setCellFactory(param -> new CustomCellFriend());
    }

    @Override
    public String getViewPath() {
        return Constants.View.FRIEND_MESSAGES_REPORT;
    }

    @Override
    public void setServiceReports(ServiceReports serviceReports) {
        this.serviceReports = serviceReports;
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pdf", "*.pdf"));
        File selectedFile = fileChooser.showSaveDialog(getStage());
        if (selectedFile != null) {
            try {
                Friend selectedFriend=friendsListView.getSelectionModel().getSelectedItem();
                serviceReports.createFriendMessagesReport(selectedFile.getPath(),multiDatePicker.getStartDate(),multiDatePicker.getEndDate(),idLoggedUser,selectedFriend.getUser().getId());
            }catch(ServiceException serviceException){
                AlertMessage.showErrorMessage(serviceException.getMessage());
            }
        }
    }

    public void searchFriendsByName() {
        friendsObservableList.setAll(serviceFriends.searchFriendsByName(idLoggedUser, searchFriendsTextField.getText()));
    }

    public static class CustomCellFriend extends ListCell<Friend> {
        HBox mainHBox = new HBox();
        Label friendName = new Label();
        Friend friend;

        public CustomCellFriend() {
            friendName.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 16");
            mainHBox.getChildren().setAll(friendName);
            this.setStyle("-fx-background-color: transparent;-fx-border-color: transparent");
        }

        @Override
        protected void updateItem(Friend item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                friend = null;
                setGraphic(null);
            } else {
                friend = item;
                friendName.setText(friend.getUser().getLastName() + " " + friend.getUser().getFirstName());
                setGraphic(mainHBox);
            }
        }


    }
}
