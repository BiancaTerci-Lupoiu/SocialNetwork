package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import project.lab6.controllers.Controller;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.UserChatInfoDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.Observer;
import project.lab6.utils.observer.ObservableChatDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatDetailsController extends Controller implements Initializable, Observer<ChatDTO> {
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
        private final ObservableChatDTO observableChatDTO;

        public CustomCellChat(ServiceMessages serviceMessages, ObservableChatDTO observableChatDTO) {
            this.serviceMessages = serviceMessages;
            this.observableChatDTO = observableChatDTO;
            userImage.setFitWidth(50);
            userImage.setFitHeight(50);
            nicknameLabel.setStyle("-fx-font-family: Cambria Bold; -fx-text-fill: #5c0e63; -fx-background-color: transparent; -fx-font-size: 20");
            setStyle("-fx-background-color: #ccccff;-fx-border-color: transparent");
            changeNickname.setStyle("-fx-background-color: #5c0e63;-fx-font-family: Cambria;-fx-font-size: 14;-fx-text-fill: white;-fx-background-radius: 10;-fx-border-radius: 10");
            horizontalBox.getChildren().addAll(userImage, nicknameLabel);
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            rootAnchor.getChildren().addAll(horizontalBox, changeNickname);
            AnchorPane.setRightAnchor(changeNickname, 20d);
            AnchorPane.setTopAnchor(changeNickname,25d);
            changeTextField.setOnKeyPressed(keyEvent ->
            {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    serviceMessages.changeNickname(userInfo.getIdChat(),
                            userInfo.getUser().getId(),
                            changeTextField.getText());
                    nicknameLabel.setText(changeTextField.getText());
                    horizontalBox.getChildren().set(1, nicknameLabel);
                    Long id = observableChatDTO.getChat().getIdChat();
                    observableChatDTO.setChat(serviceMessages.getChatDTO(id));
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
    private HBox hboxButtons;
    @FXML
    private ListView<UserChatInfoDTO> listView;
    @FXML
    private Label chatNameLabel;

    private final Long idLoggerUser;
    private final ServiceMessages serviceMessages;
    private final ServiceFriends serviceFriends;
    private final ObservableChatDTO observableChatDTO;

    public ChatDetailsController(Long idLoggerUser, ServiceFriends serviceFriends, ServiceMessages serviceMessages, ObservableChatDTO observableChatDTO) {
        this.idLoggerUser = idLoggerUser;
        this.serviceMessages = serviceMessages;
        this.serviceFriends = serviceFriends;
        this.observableChatDTO = observableChatDTO;
        observableChatDTO.addObserver(this);
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
        if(observableChatDTO.getChat().isPrivateChat())
            hboxButtons.getChildren().remove(0);
        updateChat(observableChatDTO.getChat());

    }

    public void addUserToChat(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new AddMemberController(serviceMessages, serviceFriends, idLoggerUser, observableChatDTO));
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void changeColor(ActionEvent actionEvent) {
    }
}
