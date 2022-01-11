package project.lab6.controllers.messages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.lab6.controllers.Controller;
import project.lab6.domain.dtos.ChatDTO;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.factory.Factory;
import project.lab6.service.ServiceFriends;
import project.lab6.service.ServiceMessages;
import project.lab6.setter.SetterServiceFriends;
import project.lab6.setter.SetterServiceMessages;
import project.lab6.utils.Constants;
import project.lab6.utils.observer.ObservableChatDTO;
import project.lab6.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class ConversationController extends Controller implements Observer<ChatDTO>, SetterServiceFriends, SetterServiceMessages {
    private final ObservableChatDTO observableChatDTO;
    private final Long idLoggedUser;
    private final MainChatController mainChatController;
    private final ObservableList<MessageDTO> messageDTOList = FXCollections.observableArrayList();
    private final MessageDTO messageToReply;
    private ServiceFriends serviceFriends;
    private ServiceMessages serviceMessages;
    @FXML
    public Label groupNameLabel;
    @FXML
    public ListView<MessageDTO> listViewMessages;
    @FXML
    public TextField typeMessageTextField;
    @FXML
    public Button sendMessageButton;
    @FXML
    public Label labelMessageToReply;
    @FXML
    public ImageView userImage;
    @FXML
    public VBox mainVBox;
    @FXML
    public HBox hBoxReplyBar;
    @FXML
    public Button cancelReplyButton;
    boolean isVisibleReplyBar = true;

    /**
     * Creates a conversation controller and it opens for replying to the message specified
     */
    public ConversationController(ObservableChatDTO observableChatDTO, Long idLoggedUser, MainChatController mainChatController, MessageDTO messageToReply) {
        this.observableChatDTO = observableChatDTO;
        observableChatDTO.addObserver(this);
        this.idLoggedUser = idLoggedUser;
        this.mainChatController = mainChatController;
        this.messageToReply = messageToReply;
    }

    /**
     * Creates a conversation controller and doesn't show a message to reply to
     */
    public ConversationController(ObservableChatDTO observableChatDTO, Long idLoggedUser, MainChatController mainChatController) {
        this(observableChatDTO, idLoggedUser, mainChatController, null);
    }

    @Override
    public void update(ChatDTO newValue) {
        groupNameLabel.setText(newValue.getName(idLoggedUser));
        messageDTOList.setAll(newValue.getMessages());
    }

    @Override
    public String getViewPath() {
        return Constants.View.CONVERSATION;
    }

    public void chatInfoButtonClick() throws IOException {
        FXMLLoader loader = Factory.getInstance().getLoader(new ChatDetailsController(idLoggedUser, observableChatDTO));
        Scene scene = new Scene(loader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void initialize() {
        ChatDTO chatDTO = observableChatDTO.getChat();
        String chatColor = convertColorToString(chatDTO.getColor());
        cancelReplyButton.setStyle("-fx-text-fill: white;-fx-font-size: 12;-fx-border-radius: 30;-fx-background-radius: 30;-fx-background-color: black;-fx-font-family: Cambria Bold");
        listViewMessages.setCellFactory(param -> new CustomCellMessage(idLoggedUser, this::setReplyBarVisible, labelMessageToReply, chatColor, mainChatController));
        listViewMessages.setStyle("-fx-background-color:" + chatColor);
        mainVBox.setStyle("-fx-background-color:" + chatColor);
        typeMessageTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                sendMessageAction();
            }
        });
        hBoxReplyBar.setSpacing(10);
        hBoxReplyBar.setStyle("-fx-padding: 0px 10px 0px 0px");
        userImage.setImage(new Image("project/lab6/images/icon-chat-basic.png"));
        listViewMessages.setItems(messageDTOList);
        if (messageToReply != null) {
            labelMessageToReply.setText("Reply to:  " + messageToReply.getText());
            labelMessageToReply.setId(messageToReply.getId().toString());
        } else {
            setReplyBarVisible(false);
        }
        update(chatDTO);
    }

    private String convertColorToString(Color color) {
        int r = (int) (255 * color.getRed());
        int g = (int) (255 * color.getGreen());
        int b = (int) (255 * color.getBlue());
        int a = (int) (255 * color.getOpacity());
        return String.format("#%02x%02x%02x%02x", r, g, b, a);
    }

    public void sendMessageAction() {
        if (!typeMessageTextField.getText().isEmpty()) {
            Long idChat = observableChatDTO.getChat().getIdChat();
            if (!isVisibleReplyBar) {
                serviceMessages.sendMessageInChat(idChat, idLoggedUser, typeMessageTextField.getText(), LocalDateTime.now());
            } else {
                Long idMessageToReply = Long.parseLong(labelMessageToReply.getId());
                serviceMessages.replyToMessage(idChat, idLoggedUser, idMessageToReply, typeMessageTextField.getText(), LocalDateTime.now());
                cancelReplyAction();
            }
            messageDTOList.setAll(serviceMessages.getChatDTO(idChat).getMessages());
            typeMessageTextField.setText("");
        }
    }

    private void setReplyBarVisible(boolean visible) {
        if (visible == isVisibleReplyBar)
            return;
        isVisibleReplyBar = visible;
        if (visible) {
            mainVBox.getChildren().add(2, hBoxReplyBar);
        } else {
            mainVBox.getChildren().remove(2);
        }
    }

    public void cancelReplyAction() {
        setReplyBarVisible(false);
    }

    @Override
    public void setServiceFriends(ServiceFriends serviceFriends) {
        this.serviceFriends = serviceFriends;
    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    public static class CustomCellMessage extends ListCell<MessageDTO> {
        HBox horizontalBox = new HBox();
        Label messageText = new Label();
        Label repliedMessageText = new Label();
        VBox verticalBox = new VBox();
        Label dateLabel = new Label();
        Label userNameLabel = new Label();
        MessageDTO message;
        Long idLoggedUser;
        Label labelReplyShownAboveTypeText;
        Button replyInChatButton = new Button();
        Button replyInPrivateButton = new Button();
        HBox hBoxButtonsReply = new HBox();
        String cellColor;
        Consumer<Boolean> showReplyBar;
        MainChatController mainChatController;

        public CustomCellMessage(Long idLoggedUser, Consumer<Boolean> showReplyBar, Label labelShownAboveTypeText, String cellColor, MainChatController mainChatController) {
            this.idLoggedUser = idLoggedUser;
            this.labelReplyShownAboveTypeText = labelShownAboveTypeText;
            this.cellColor = cellColor;
            this.showReplyBar = showReplyBar;
            this.mainChatController = mainChatController;
            verticalBox.setMaxWidth(300.0);
            messageText.setWrapText(true);
            repliedMessageText.setWrapText(true);
            this.setStyle("-fx-background-color: " + cellColor + ";-fx-border-color: transparent");
            hBoxButtonsReply.setSpacing(5);
            horizontalBox.hoverProperty().addListener((observable, oldValue, newValue) -> hBoxButtonsReply.setVisible(newValue));
            hBoxButtonsReply.setVisible(false);
            replyInChatButton.setText("Reply");
            replyInPrivateButton.setText("Private reply");
            replyInChatButton.setStyle("-fx-font-family: Cambria Bold;-fx-font-size: 13;-fx-background-color: #5900b3;-fx-text-fill: white;-fx-border-radius: 15;-fx-background-radius: 15");
            replyInPrivateButton.setStyle("-fx-font-family: Cambria Bold;-fx-font-size: 13;-fx-background-color: #5900b3;-fx-text-fill: white;-fx-border-radius: 15;-fx-background-radius: 15");
            repliedMessageText.setStyle("-fx-font-family: Cambria;-fx-text-fill:#696766;-fx-font-size: 16;-fx-background-color: #d1b3ff;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
            dateLabel.setStyle("-fx-background-color: transparent;-fx-font-family: Cambria;-fx-font-size: 12;-fx-padding: 2px 10px 2px 0px");
            userNameLabel.setStyle("-fx-background-color: transparent;-fx-font-family: Cambria;-fx-font-size: 14");
            horizontalBox.getChildren().add(verticalBox);
            labelShownAboveTypeText.setStyle("-fx-font-size: 16;-fx-font-family: Cambria Bold;-fx-text-fill: #696766");
            repliedMessageText.setStyle("-fx-font-family: Cambria;-fx-text-fill:#696766;-fx-font-size: 16;-fx-background-color: #d1b3ff;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");


            replyInChatButton.setOnAction(event -> {
                labelShownAboveTypeText.setText("Reply to:  " + messageText.getText());
                labelShownAboveTypeText.setId(message.getId().toString());
                showReplyBar.accept(true);
            });
            replyInPrivateButton.setOnAction(event -> {
                mainChatController.setConversationView(mainChatController.getServiceMessages().getOrCreatePrivateChatBetweenUsers(idLoggedUser, message.getUserFromInfo().getUser().getId()).getId(), message);
                showReplyBar.accept(true);
            });
        }

        private void addLabelWithUserName() {
            userNameLabel.setText(message.getUserFromInfo().getNickname());
            verticalBox.getChildren().add(userNameLabel);
        }

        private void addLabelWithRepliedMessage() {
            verticalBox.getChildren().add(repliedMessageText);
            repliedMessageText.setText(message.getRepliedMessage().getText());
        }

        private void addLabelWithMessage() {
            messageText.setText(message.getText());
            verticalBox.getChildren().add(messageText);
        }

        private void alignComponentsToRight() {
            horizontalBox.setAlignment(Pos.CENTER_RIGHT);
            messageText.setStyle("-fx-background-color: #b3b3ff;-fx-font-size: 18;-fx-font-family: Cambria;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
            hBoxButtonsReply.getChildren().setAll(replyInChatButton, dateLabel);
            verticalBox.setAlignment(Pos.CENTER_RIGHT);
        }

        private void alignComponentsToLeft() {
            horizontalBox.setAlignment(Pos.CENTER_LEFT);
            verticalBox.setAlignment(Pos.CENTER_LEFT);
            hBoxButtonsReply.getChildren().setAll(dateLabel, replyInChatButton);
            if (!message.getChat().isPrivateChat())
                hBoxButtonsReply.getChildren().add(replyInPrivateButton);
            messageText.setStyle("-fx-background-color: #aa80ff;-fx-font-size: 18;-fx-font-family: Cambria;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
        }

        @Override
        protected void updateItem(MessageDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                message = null;
                setGraphic(null);
            } else {
                message = item;
                verticalBox.getChildren().clear();
                dateLabel.setText(message.getDate().format(Constants.DATETIME_FORMATTER));
                boolean isLoggedUser = idLoggedUser.equals(message.getUserFromInfo().getUser().getId());
                if (!isLoggedUser) {
                    addLabelWithUserName();
                }
                if (message.getRepliedMessage() != null) {
                    addLabelWithRepliedMessage();
                }
                addLabelWithMessage();
                if (isLoggedUser) {
                    alignComponentsToRight();
                } else {
                    alignComponentsToLeft();
                }
                verticalBox.getChildren().add(hBoxButtonsReply);
                setGraphic(horizontalBox);
            }
        }
    }

}
