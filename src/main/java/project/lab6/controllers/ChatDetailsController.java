package project.lab6.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;

import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

public class ChatDetailsController extends Controller implements Initializable {
    @Override
    public String getViewPath() {
        return Constants.View.CHAT_DETAILS;
    }

    public static class CustomCellChat extends ListCell<UserChatInfoDTO> {
        private AnchorPane rootAnchor = new AnchorPane();
        private HBox horizontalBox = new HBox();
        private Label nicknameLabel = new Label();
        private TextField changeTextField = new TextField();
        private ImageView userImage = new ImageView();
        private Button changeNickname = new Button("Change nickname");
        private final ServiceMessages serviceMessages;
        private UserChatInfoDTO userInfo = null;

        public CustomCellChat(ServiceMessages serviceMessages) {
            this.serviceMessages = serviceMessages;
            userImage.setFitWidth(50);
            userImage.setFitHeight(50);
            nicknameLabel.setStyle("-fx-font-family: Cambria; -fx-background-color: transparent; -fx-font-size: 20");
            setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");

            horizontalBox.getChildren().addAll(userImage, nicknameLabel);
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            rootAnchor.getChildren().addAll(horizontalBox, changeNickname);
            AnchorPane.setRightAnchor(changeNickname, 20d);
            changeTextField.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    serviceMessages.changeNickname(userInfo.getIdChat(),
                            userInfo.getUser().getId(),
                            changeTextField.getText());
                    nicknameLabel.setText(changeTextField.getText());
                    horizontalBox.getChildren().set(1, nicknameLabel);
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    horizontalBox.getChildren().set(1, nicknameLabel);
                }
            });
            changeTextField.focusedProperty().addListener((observable, oldValue, newValue) ->
            {
                if (oldValue && !newValue)
                    horizontalBox.getChildren().set(1, nicknameLabel);
            });
            changeNickname.setOnAction(event ->
            {
                changeTextField.setText(nicknameLabel.getText());
                horizontalBox.getChildren().set(1, changeTextField);
                changeTextField.requestFocus();
            });
        }

        @Override
        protected void updateItem(UserChatInfoDTO item, boolean empty) {
            super.updateItem(item, empty);
            userInfo = item;
            if (empty) {
                setGraphic(null);
            } else {
                nicknameLabel.setText(item.getNickname());
                userImage.setImage(new Image("project/lab6/images/icon-chat-basic.png"));
                setGraphic(rootAnchor);
            }
        }
    }

    private final ObservableList<UserChatInfoDTO> userChatInfos = FXCollections.observableArrayList();

    @FXML
    private ListView<UserChatInfoDTO> listView;
    @FXML
    private Label chatNameLabel;

    private final Long idLoggerUser;
    private final ServiceMessages serviceMessages;
    private final Long idChat;

    public ChatDetailsController(Long idLoggerUser, ServiceMessages serviceMessages, Long idChat) {
        this.idLoggerUser = idLoggerUser;
        this.serviceMessages = serviceMessages;
        this.idChat = idChat;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatDTO chat = serviceMessages.getChatDTO(idChat);
        chatNameLabel.setText(chat.getName(idLoggerUser));
        userChatInfos.setAll(chat.getUsersInfo());
        listView.setCellFactory(param -> new CustomCellChat(serviceMessages));
        listView.setItems(userChatInfos);
    }

    public void addUserToChat(ActionEvent actionEvent) {
    }

    public void changeColor(ActionEvent actionEvent) {
    }
}
