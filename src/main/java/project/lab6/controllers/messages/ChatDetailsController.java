package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.lab6.controllers.AlertMessage;
import project.lab6.controllers.Controller;
import project.lab6.controllers.HasTitleBar;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceException;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.ObservableResource;
import project.lab6.utils.observer.Observer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatDetailsController extends Controller implements Initializable, Observer<ChatDTO>, SetterServiceMessages, HasTitleBar {
    private final ObservableList<UserChatInfoDTO> userChatInfos = FXCollections.observableArrayList();
    private final Long idLoggerUser;
    private final ObservableResource<ChatDTO> observableChatDTO;
    @FXML
    public HBox hBoxHeading;
    @FXML
    public Circle circle;
    @FXML
    public Button changePictureButton;
    @FXML
    public ColorPicker colorPicker;
    private ServiceMessages serviceMessages;
    @FXML
    private HBox hBoxButtons;
    @FXML
    private ListView<UserChatInfoDTO> listView;
    @FXML
    private Label chatNameLabel;

    public ChatDetailsController(Long idLoggerUser, ObservableResource<ChatDTO> observableChatDTO) {
        this.idLoggerUser = idLoggerUser;
        this.observableChatDTO = observableChatDTO;
        observableChatDTO.addObserver(this);
    }

    @Override
    public String getViewPath() {
        return Constants.View.CHAT_DETAILS;
    }

    @Override
    public void update(ChatDTO newValue) {
        updateChat(newValue);
    }

    private void updateChat(ChatDTO newChat) {
        chatNameLabel.setText(newChat.getName(idLoggerUser));
        userChatInfos.setAll(newChat.getUsersInfo());
        listView.setItems(userChatInfos);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(param -> new CustomCellChat(serviceMessages, observableChatDTO));
        if (observableChatDTO.getResource().isPrivateChat()) {
            hBoxButtons.getChildren().remove(0);
            hBoxHeading.getChildren().remove(2);
        }
        updateChat(observableChatDTO.getResource());
        colorPicker.setValue(observableChatDTO.getResource().getColor());
        colorPicker.setOnAction(someEvent -> {
            ChatDTO oldChat = observableChatDTO.getResource();
            serviceMessages.changeChatColor(oldChat.getIdChat(), colorPicker.getValue());
            ChatDTO newChat = serviceMessages.getChatDTO(oldChat.getIdChat());
            observableChatDTO.setResource(newChat);
        });
        ChatDTO chatDTO = observableChatDTO.getResource();
        Image userImage = chatDTO.getImage(idLoggerUser);
        circle.setFill(new ImagePattern(userImage));
        circle.setStrokeWidth(2);
        circle.setRadius(30);
        circle.setStroke(Color.web("#5c0e63"));
    }

    public void addUserToChat() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new AddMemberController(idLoggerUser, observableChatDTO));
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(loader.load(), 600, 400);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.showAndWait();
    }


    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    public void changePictureAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Pictures(.png,.jpg)", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile == null)
            return;
        try {
            ChatDTO oldChatDTO = observableChatDTO.getResource();
            serviceMessages.saveChatImage(oldChatDTO.getIdChat(), selectedFile.getPath());
            ChatDTO newChatDTO = serviceMessages.getChatDTO(oldChatDTO.getIdChat());
            observableChatDTO.setResource(newChatDTO);
            Image userImage = newChatDTO.getImage(idLoggerUser);
            circle.setFill(new ImagePattern(userImage));
        } catch (ServiceException serviceException) {
            AlertMessage.showErrorMessage(serviceException.getMessage());
        }
    }

    public static class CustomCellChat extends ListCell<UserChatInfoDTO> {
        private final AnchorPane rootAnchor = new AnchorPane();
        private final HBox horizontalBox = new HBox();
        private final Label nicknameLabel = new Label();
        private final TextField changeTextField = new TextField();
        private final ImageView userImage = new ImageView();
        private final Button changeNickname = new Button("Change nickname");
        private final ServiceMessages serviceMessages;
        private final ObservableResource<ChatDTO> observableChatDTO;
        private UserChatInfoDTO userInfo = null;

        public CustomCellChat(ServiceMessages serviceMessages, ObservableResource<ChatDTO> observableChatDTO) {
            this.serviceMessages = serviceMessages;
            this.observableChatDTO = observableChatDTO;
            userImage.setFitWidth(50);
            userImage.setFitHeight(50);
            nicknameLabel.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63; -fx-background-color: transparent; -fx-font-size: 20");
            setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");
            changeNickname.setStyle("-fx-background-color: #5c0e63;-fx-font-family: Cambria;-fx-font-size: 14;-fx-text-fill: white;-fx-background-radius: 10;-fx-border-radius: 10");
            horizontalBox.getChildren().addAll(userImage, nicknameLabel);
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER_RIGHT);
            box.getChildren().add(changeNickname);
            rootAnchor.getChildren().addAll(horizontalBox, box);
            box.setPrefHeight(50);
            AnchorPane.setRightAnchor(box, 20d);
            //AnchorPane.setTopAnchor(changeNickname, 25d);
            changeTextField.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    serviceMessages.changeNickname(userInfo.getIdChat(),
                            userInfo.getUser().getId(),
                            changeTextField.getText());
                    nicknameLabel.setText(changeTextField.getText());
                    horizontalBox.getChildren().set(1, nicknameLabel);
                    Long id = observableChatDTO.getResource().getIdChat();
                    observableChatDTO.setResource(serviceMessages.getChatDTO(id));
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
}
