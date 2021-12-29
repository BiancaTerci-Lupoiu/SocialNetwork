package project.lab6.controllers;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

        public CustomCellMessage(Long idLoggedUser) {
            super();
            this.idLoggedUser = idLoggedUser;
            verticalBox.getChildren().addAll(userNameLabel, repliedMessageText, messageText, dateLabel);
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
                message = item;
                messageText.setText(message.getText());
                dateLabel.setText(message.getDate().format(Constants.DATETIME_FORMATTER));
                if (message.getRepliedMessage() != null) {
                    repliedMessageText.setVisible(true);
                    repliedMessageText.setText(message.getRepliedMessage().getText());
                } else {
                    repliedMessageText.setVisible(false);
                }
                if (idLoggedUser.equals(message.getUserFrom().getUser().getId())) {
                    horizontalBox.setAlignment(Pos.TOP_RIGHT);
                    userNameLabel.setVisible(false);
                } else {
                    horizontalBox.setAlignment(Pos.TOP_LEFT);
                    userNameLabel.setVisible(true);
                    userNameLabel.setText(message.getUserFrom().getNickname());
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

    private Long idChat;
    private ServiceMessages serviceMessages;
    private Long idLoggedUser;
    private ObservableList<MessageDTO> messageDTOList= FXCollections.observableArrayList();

    public void setIdChat(Long idChat) {
        this.idChat = idChat;
        groupNameLabel.setText(serviceMessages.getChatDTO(idChat).getName(idLoggedUser));
        messageDTOList.setAll(serviceMessages.getChatDTO(idChat).getMessages());
        listViewMessages.setItems(messageDTOList);
        listViewMessages.setCellFactory(new Callback<ListView<MessageDTO>, ListCell<MessageDTO>>() {
            @Override
            public ListCell<MessageDTO> call(ListView<MessageDTO> param) {
                return new CustomCellMessage(idLoggedUser);
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
        if(!typeMessageTextField.getText().isEmpty())
        {
            serviceMessages.sendMessageInChat(idChat,idLoggedUser,typeMessageTextField.getText(), LocalDateTime.now());
            messageDTOList.setAll(serviceMessages.getChatDTO(idChat).getMessages());
        }
    }
}
