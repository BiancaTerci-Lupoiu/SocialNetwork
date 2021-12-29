package project.lab6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceMessages;
import project.lab6.setter_interface.local.SetterIdChat;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatDetailsController implements Initializable, SetterIdLoggedUser, SetterServiceMessages, SetterIdChat {

    public static class CustomCellChat extends ListCell<UserChatInfoDTO> {
        AnchorPane rootAnchor = new AnchorPane();
        HBox horizontalBox = new HBox();
        Label nicknameLabel = new Label();
        ImageView userImage = new ImageView();
        Button changeNickname = new Button("Change nickname");

        public CustomCellChat() {
            userImage.setFitWidth(50);
            userImage.setFitHeight(50);
            nicknameLabel.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 20");
            setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");

            horizontalBox.getChildren().addAll(userImage, nicknameLabel);
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            rootAnchor.getChildren().addAll(horizontalBox, changeNickname);
            AnchorPane.setRightAnchor(changeNickname, 20d);
        }

        @Override
        protected void updateItem(UserChatInfoDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                nicknameLabel.setText(item.getNickname());
                userImage.setImage(new Image("project/lab6/images/icon-chat-basic.png"));
                setGraphic(rootAnchor);
            }
        }
    }

    private ObservableList<UserChatInfoDTO> userChatInfos = FXCollections.observableArrayList();

    @FXML
    private ListView<UserChatInfoDTO> listView;
    @FXML
    private Label chatNameLabel;

    private Long idLoggerUser;
    private ServiceMessages serviceMessages;
    private Long idChat;

    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.idLoggerUser = idLoggedUser;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    @Override
    public void setIdChat(Long idChat) {
        this.idChat = idChat;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatDTO chat = serviceMessages.getChatDTO(idChat);
        chatNameLabel.setText(chat.getName(idLoggerUser));
        userChatInfos.setAll(chat.getUsersInfo());
        listView.setCellFactory(param -> new CustomCellChat());
        listView.setItems(userChatInfos);
    }

    public void setStage(Stage stage)
    {
        stage.widthProperty().addListener((obs, oldValue,newValue)->
                listView.setPrefWidth(newValue.doubleValue()));
    }

    public void addUserToChat(ActionEvent actionEvent) {
    }

    public void changeColor(ActionEvent actionEvent) {
    }
}
