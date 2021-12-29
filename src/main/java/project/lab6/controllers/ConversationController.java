package project.lab6.controllers;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import project.lab6.domain.dtos.MessageDTO;
import project.lab6.service.ServiceMessages;
import project.lab6.setter_interface.SetterIdLoggedUser;
import project.lab6.setter_interface.SetterServiceMessages;
import project.lab6.utils.Constants;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ConversationController implements SetterServiceMessages, SetterIdLoggedUser {


    public static class CustomCellMessage extends ListCell<MessageDTO> {
        HBox horizontalBox = new HBox();
        Label messageText = new Label();
        Label repliedMessageText = new Label();
        VBox verticalBox = new VBox();
        Label dateLabel = new Label();
        Label userNameLabel = new Label();
        MessageDTO message;
        Long idLoggedUser;
        Label labelShownAboveTypeText;
        Button replyInChatButton = new Button();
        Button replyInPrivateButton = new Button();
        HBox hBoxButtons = new HBox();

        public CustomCellMessage(Long idLoggedUser, Label labelShownAboveTypeText) {
            super();
            this.idLoggedUser = idLoggedUser;
            this.labelShownAboveTypeText = labelShownAboveTypeText;
            hBoxButtons.setVisible(false);
            horizontalBox.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                if (newValue) {
                    hBoxButtons.setVisible(true);
                } else {
                    hBoxButtons.setVisible(false);
                }
            });
            replyInChatButton.setText("Reply");
            replyInChatButton.setStyle("-fx-font-family: Cambria;-fx-font-size: 12;-fx-background-color: ");
            repliedMessageText.setStyle("-fx-font-family: Cambria;-fx-text-fill:#696766;-fx-font-size: 16;-fx-background-color: #d1b3ff;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
            dateLabel.setStyle("-fx-background-color: transparent;-fx-font-family: Cambria;-fx-font-size: 12;-fx-padding: 2px 15px 2px 0px");
            userNameLabel.setStyle("-fx-background-color: transparent;-fx-font-family: Cambria;-fx-font-size: 14");
            hBoxButtons.getChildren().addAll(dateLabel, replyInChatButton, replyInPrivateButton);
            verticalBox.getChildren().addAll(userNameLabel, repliedMessageText, messageText, hBoxButtons);
            repliedMessageText.setVisible(false);
            horizontalBox.getChildren().add(verticalBox);
            horizontalBox.setAlignment(Pos.TOP_LEFT);
        }

        @Override
        protected void updateItem(MessageDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                message = null;
                setGraphic(null);
            } else {
                replyInChatButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        labelShownAboveTypeText.setStyle("-fx-font-size: 12");
                        labelShownAboveTypeText.setVisible(true);
                        labelShownAboveTypeText.setText(messageText.getText());
                        labelShownAboveTypeText.setId(message.getId().toString());
                    }
                });

                message = item;
                messageText.setText(message.getText());
                dateLabel.setText(message.getDate().format(Constants.DATETIME_FORMATTER));
                if (message.getRepliedMessage() != null) {
                    repliedMessageText.setVisible(true);
                    repliedMessageText.setStyle("-fx-font-family: Cambria;-fx-text-fill:#696766;-fx-font-size: 16;-fx-background-color: #d1b3ff;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
                    repliedMessageText.setText(message.getRepliedMessage().getText());
                } else {
                    repliedMessageText.setVisible(false);
                    repliedMessageText.setStyle("-fx-font-size: 1");
                }
                if (idLoggedUser.equals(message.getUserFrom().getUser().getId())) {
                    userNameLabel.setStyle("-fx-font-size: 1");
                    horizontalBox.setAlignment(Pos.TOP_RIGHT);
                    userNameLabel.setVisible(false);
                    messageText.setStyle("-fx-background-color: #b3b3ff;-fx-font-size: 18;-fx-font-family: Cambria;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
                    dateLabel.setStyle("-fx-padding: 2px 0px 2px 15px");
                    hBoxButtons.getChildren().setAll(replyInChatButton, replyInPrivateButton, dateLabel);
                } else {
                    horizontalBox.setAlignment(Pos.TOP_LEFT);
                    userNameLabel.setVisible(true);
                    userNameLabel.setText(message.getUserFrom().getNickname());
                    messageText.setStyle("-fx-background-color: #bb99ff;-fx-font-size: 18;-fx-font-family: Cambria;-fx-border-radius: 10 10 10 10;-fx-background-radius: 10 10 10 10;-fx-padding: 2px 15px 2px 15px");
                }
                setGraphic(horizontalBox);
            }
        }
    }

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

    private Long idChat;
    private ServiceMessages serviceMessages;
    private Long idLoggedUser;
    private ObservableList<MessageDTO> messageDTOList = FXCollections.observableArrayList();


    public void setIdChat(Long idChat) {
        this.idChat = idChat;
        groupNameLabel.setText(serviceMessages.getChatDTO(idChat).getName(idLoggedUser));
        messageDTOList.setAll(serviceMessages.getChatDTO(idChat).getMessages());
        listViewMessages.setItems(messageDTOList);
        labelMessageToReply.setVisible(false);
        listViewMessages.setCellFactory(new Callback<ListView<MessageDTO>, ListCell<MessageDTO>>() {
            @Override
            public ListCell<MessageDTO> call(ListView<MessageDTO> param) {
                return new CustomCellMessage(idLoggedUser, labelMessageToReply);
            }
        });
        //Color color=serviceMessages.getChatDTO(idChat).getColor();
        //String hex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        //listViewMessages.setStyle("-fx-background-color:"+hex);

    }

    @Override
    public void setServiceMessages(ServiceMessages serviceMessages) {
        this.serviceMessages = serviceMessages;
    }

    public void initialize() {
        //groupNameLabel.setText(serviceMessages.getChatDTO(idChat).getName(idLoggedUser));
    }

    /**
     * sets the id of the logged user to idLoggedUser
     *
     * @param idLoggedUser
     */
    @Override
    public void setIdLoggedUser(Long idLoggedUser) {
        this.idLoggedUser = idLoggedUser;
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        if (!typeMessageTextField.getText().isEmpty()) {
            if (!labelMessageToReply.isVisible()) {
                serviceMessages.sendMessageInChat(idChat, idLoggedUser, typeMessageTextField.getText(), LocalDateTime.now());
            } else {
                Long idMessageToReply = Long.parseLong(labelMessageToReply.getId());
                serviceMessages.replyToMessage(idLoggedUser, idMessageToReply, typeMessageTextField.getText(), LocalDateTime.now());
                labelMessageToReply.setVisible(false);
                labelMessageToReply.setStyle("-fx-font-size: 1");
            }
            messageDTOList.setAll(serviceMessages.getChatDTO(idChat).getMessages());
            typeMessageTextField.setText("");
        }
    }
}
